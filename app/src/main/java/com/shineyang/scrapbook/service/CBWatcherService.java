package com.shineyang.scrapbook.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;
import com.shineyang.scrapbook.utils.ApplicationUtil;
import com.shineyang.scrapbook.utils.DateUtils;


/**
 * Created by ShineYang on 2016/11/7.
 */

public class CBWatcherService extends Service {
    private Context mContext;
    private NotificationManagerCompat notificationManager;
    private ClipboardManager clipboardManager;
    protected boolean temporaryStop = false;
    private long previousTime = 0;
    private AppBean appBean;
    private String packageName;
    private String appName;
    private Drawable appIcon;

    private ClipboardManager.OnPrimaryClipChangedListener listener = new ClipboardManager.OnPrimaryClipChangedListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onPrimaryClipChanged() {
            /**
             * 此处是防止间隔太短导致复制两次内容
             * 参考:http://www.jianshu.com/p/ed4637bfeb05
             */
            long now = System.currentTimeMillis();
            if (now - previousTime < 200) {
                previousTime = now;
                return;
            }
            previousTime = now;
            performClipboardCheck();
        }
    };

    @Override
    public void onCreate() {
        Log.v("cbservice", "-------oncreate cb service");
        mContext = this;
        notificationManager = NotificationManagerCompat.from(this);
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(listener);

        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void performClipboardCheck() {
        Log.v("cbservice", "-------performClipboardCheck");
        if (temporaryStop) return;
        if (!clipboardManager.hasPrimaryClip()) return;
        String clipContent, date;
        ListBean listBean;
        try {
            //Don't use CharSequence .toString()!
            CharSequence charSequence = clipboardManager.getPrimaryClip().getItemAt(0).getText();
            clipContent = String.valueOf(charSequence);
        } catch (Error ignored) {
            return;
        }
        if (clipContent.trim().isEmpty()) return;
        if (clipContent.equals("null")) return;

        getAppInfo();//获取当前app的信息(包名,程序名,图标)
        date = DateUtils.getCurDateAndTime();//复制时间
        listBean = new ListBean(clipContent, appName, date);
        appBean = new AppBean(appName, packageName, Environment.getExternalStorageDirectory() + "/com.shineyang.scrapbook/" + appName + ".png");
        AppBeanDao appBeanDao = GreenDaoManager.getInstance().getSession().getAppBeanDao();

        //应用名去重
        if (appBeanDao.queryBuilder().where(AppBeanDao.Properties.AppName.eq(appName)).list().size() >= 1) {
            Log.v("cbservice","-------已添加过该应用标签");
        }else{
            appBeanDao.insert(appBean);
        }

        ListBeanDao listBeanDao = GreenDaoManager.getInstance().getSession().getListBeanDao();
        listBeanDao.insert(listBean);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getAppInfo() {
        packageName = ApplicationUtil.getForegroundPackageName(getApplicationContext());
        appName = ApplicationUtil.getProgramNameByPackageName(getApplicationContext(), packageName);
        Log.v("cbservice", "-------packageName:" + packageName + "(" + appName + ")");
        appIcon = ApplicationUtil.getAppIconByPackageName(getApplicationContext(), packageName);
        ApplicationUtil.saveIconToDir(ApplicationUtil.drawableToBitmap(appIcon), appName);
    }

    //添加剪贴目标app数据到数据库，做去重处理
    public void addAppBean() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
