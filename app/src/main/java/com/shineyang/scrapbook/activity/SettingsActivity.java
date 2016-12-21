package com.shineyang.scrapbook.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.service.CBWatcherService;

public class SettingsActivity extends MyPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String PREF_START_SERVICE = "open_service_switch";
    //public final static String PREF_OPEN_PACKAGE_USAGE_STATS = "open_package_usage_stats_switch";
    public final static String PREF_OPEN_NOTIFICATION = "open_notification_switch";
    public final static String PREF_SELECT_SEARCH_ENGINE = "search_engine_list";
    public final static String PREF_AUTO_DISMISS_NOTIFICATION = "auto_dismiss_notification_switch";
    public final static String PREF_OPEN_FLAT_WINDOW = "open_flat_window_switch";
    public final static String PREFFLAT_WINDOW_TRANSPARENCY = "flat_window_transparency_list";

    private SharedPreferences.OnSharedPreferenceChangeListener myPrefChangeListener;
    private Context context;
    private Boolean isGranted = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PREF_START_SERVICE:
                CBWatcherService.startCBService(context);
                if (!sharedPreferences.getBoolean(PREF_START_SERVICE, true)) {
                    context.stopService(new Intent(context, CBWatcherService.class));
                    break;
                }

//            case PREF_OPEN_PACKAGE_USAGE_STATS:
//                //checkUsagePermission();
////                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                    startActivityForResult(intent, 1);
//                    break;

            case PREF_OPEN_NOTIFICATION:
                break;

            case PREF_SELECT_SEARCH_ENGINE:
                break;

            case PREF_AUTO_DISMISS_NOTIFICATION:
                break;

            case PREF_OPEN_FLAT_WINDOW:
                break;

            case PREFFLAT_WINDOW_TRANSPARENCY:
                break;
        }
    }

//    public SettingsActivity() {
//        myPrefChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
//            }
//        };
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        addPreferencesFromResource(R.xml.settings);
        //preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//        if (isGranted) {
//            Toast.makeText(context, "已授权", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "未授权", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Boolean checkUsagePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode;
            mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            Log.v("granted", "--------" + String.valueOf(granted));
//            if (!granted) {
//                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 1);
//                return false;
//            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            isGranted = granted;
            if (!granted) {
                Toast.makeText(this, "请开启该权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void initSharedPrefListener() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }


}
