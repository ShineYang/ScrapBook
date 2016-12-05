package com.shineyang.scrapbook.service;

import android.app.Notification;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.adapter.NotificationWidgetAdapter;
import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;
import com.shineyang.scrapbook.utils.ApplicationUtil;
import com.shineyang.scrapbook.utils.DBUtils;
import com.shineyang.scrapbook.utils.DateUtils;
import com.thefinestartist.finestwebview.FinestWebView;


/**
 * Created by ShineYang on 2016/11/7.
 */

public class CBWatcherService extends Service {
    private final static String NOTIFICATION_GROUP = "notification_group";

    private Context mContext;
    private NotificationManagerCompat notificationManager;
    private ClipboardManager clipboardManager;
    protected boolean temporaryStop = false;
    private long previousTime = 0;
    private AppBean appBean;
    private String packageName;
    private String appName;
    private Drawable appIcon;
    private String clipContent, date;
    private AppBeanDao appBeanDao;

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
        ListBean listBean;
        try {
            //Don't use CharSequence .toString()!
            CharSequence charSequence = clipboardManager.getPrimaryClip().getItemAt(0).getText();
            clipContent = String.valueOf(charSequence);
            showNotificationWidget();

        } catch (Error ignored) {
            return;
        }
        if (clipContent.trim().isEmpty()) return;
        if (clipContent.equals("null")) return;

        getAppInfo();//获取当前app的信息(包名,程序名,图标)
        if (packageName.equals(getPackageName())) {
            Log.v("cbservice", "-------过滤掉来自本应用的复制动作");
        } else {
            date = DateUtils.getCurDateAndTime();//复制时间
            listBean = new ListBean(clipContent, appName, date);
            appBean = new AppBean(appName, packageName, Environment.getExternalStorageDirectory() + "/com.shineyang.scrapbook/" + appName + ".png");
            appBeanDao = GreenDaoManager.getInstance().getSession().getAppBeanDao();

            //应用名去重
            if (appBeanDao.queryBuilder().where(AppBeanDao.Properties.AppName.eq(appName)).list().size() >= 1) {
                Log.v("cbservice", "-------已添加过该应用标签");
            } else {
                appBeanDao.insert(appBean);
            }

            ListBeanDao listBeanDao = GreenDaoManager.getInstance().getSession().getListBeanDao();
            listBeanDao.insert(listBean);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getAppInfo() {
        packageName = ApplicationUtil.getForegroundPackageName(getApplicationContext());
        appName = ApplicationUtil.getProgramNameByPackageName(getApplicationContext(), packageName);
        Log.v("cbservice", "-------packageName:" + packageName + "(" + appName + ")");
        appIcon = ApplicationUtil.getAppIconByPackageName(getApplicationContext(), packageName);
        ApplicationUtil.saveIconToDir(ApplicationUtil.drawableToBitmap(appIcon), appName);
    }


    public void showNotificationWidget() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_view_small);
        NotificationCompat.Builder preBuildNotification = new NotificationCompat.Builder(this)
                .setContentTitle(clipContent) //title
                .setSmallIcon(R.mipmap.ic_notification_small)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                //.setContentIntent(pendingintent)
                .setOngoing(false)
                .setAutoCancel(false)
                .setContentText("下滑展开更多选项")
                .setGroup(NOTIFICATION_GROUP)
                .setGroupSummary(true);
        //.setContent(remoteViews);
        //remoteViews.setTextViewText(R.id.tv_current_clip_text, clipContent);

        NotificationWidgetAdapter adapter = new NotificationWidgetAdapter(mContext, clipContent);

        Notification n = preBuildNotification.build();


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            n.bigContentView = adapter.build();
        } else {
            Toast.makeText(getApplicationContext(), "not support your android version", Toast.LENGTH_SHORT).show();
        }
        //n.icon = R.mipmap.ic_launcher;

        notificationManager.notify(0, n);

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
