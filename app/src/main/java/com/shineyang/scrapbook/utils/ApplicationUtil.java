package com.shineyang.scrapbook.utils;

/**
 * Created by ShineYang on 2016/11/10.
 */

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ApplicationUtil {

    /**
     * 通过包名获取应用程序的名称
     *
     * @param context     Context对象
     * @param packageName 包名
     * @return 返回包名所对应的应用程序的名称
     */
    public static String getProgramNameByPackageName(Context context,
                                                     String packageName) {
        PackageManager pm = context.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 通过包名获取应用程序的图标
     *
     * @param context     Context对象
     * @param packageName 包名
     * @return 返回包名所对应的应用程序的图标
     */
    public static Drawable getAppIconByPackageName(Context context,
                                                   String packageName) {
        PackageManager pm = context.getPackageManager();
        Drawable icon = null;
        try {
            ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            icon = info.loadIcon(pm);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return icon;
    }

    public static void saveIconToDir(Bitmap bitmap, String packageName) {
        Log.v("util", "----存取目录"+Environment.getExternalStorageDirectory() + "/com.shineyang.scrapbook");

        File appDir = new File(Environment.getExternalStorageDirectory() + "/com.shineyang.scrapbook");
        if (!appDir.exists()) {
            Log.v("util", "----不存在目录，创建");
            appDir.mkdirs();
        } else {
            Log.v("util", "----已存在目录");
        }
        String fileName = packageName + ".png";
        try {
            File file = new File(appDir, fileName);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * 获取前台程序包名
     *
     * @param context Context对象
     * @return 返回包名
     */
    public static String getForegroundPackageName(Context context) {
        String topPackageName = "未知";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);
            // Sort the stats by the last time used
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (!mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }

        return topPackageName;
    }


    public static SharedPreferences getLocalSharedPreferences(Context context) {
        return context.getSharedPreferences("LocalSharedPreference", Context.MODE_PRIVATE);
    }


}