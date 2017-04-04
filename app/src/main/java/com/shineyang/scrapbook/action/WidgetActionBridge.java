package com.shineyang.scrapbook.action;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shineyang.scrapbook.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * Created by ShineYang on 2016/11/30.
 */

public class WidgetActionBridge extends IntentService {

    public final static int ACTION_SEARCH = 1;
    public final static int ACTION_PULLWORD = 2;
    public final static int ACTION_TRANSLATE = 3;
    public final static int ACTION_QR_CODE = 4;
    public final static int ACTION_SHARE = 5;
    public final static String ACTION_CODE = "0";

    public static String BASE_SEARCH_URL = "";
    public static String BAIDU_BASE_SEARCH_URL = "https://www.baidu.com/s?wd=";
    public static String GOOGLE_BASE_SEARCH_URL = "https://www.google.com/#q=";
    public static String BING_BASE_SEARCH_URL = "https://www.bing.com/search?q=";
    public final static String PREF_SELECT_SEARCH_ENGINE = "search_engine_list";


    private Intent intent;
    public Handler handler;

    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;
    RelativeLayout root_layout;

    public WidgetActionBridge() {
        super("WidgetActionBridge");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        Log.v("WidgetActionBridge", "------WidgetActionBridge on create");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent = intent;
        if (intent == null) return;
        String clipedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String action = intent.getStringExtra(Intent.EXTRA_TEXT);
        int actionCode = intent.getIntExtra(ACTION_CODE, 0);
        Log.v("WidgetActionBridge", String.valueOf(actionCode));

        switch (actionCode) {
            case ACTION_SEARCH:
                searchWithKeyWord(clipedText);
                break;
            case ACTION_PULLWORD:
                pullWord(clipedText);
                break;
            case ACTION_TRANSLATE:
                translateClipedContent();
                break;
            case ACTION_QR_CODE:
                break;
            case ACTION_SHARE:
                break;
        }

    }

    public void searchWithKeyWord(final String keyWord) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                searchWithWebView(keyWord);
            }
        });
    }

    public void searchClipedContent() {

    }

    public void pullWord(String content) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("pullword://?extra_text=" + URLEncoder.encode(content, "utf-8"))));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void translateClipedContent() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                createTranslateWindow();
            }
        });
    }

    public void createQRCode() {

    }

    public void searchWithWebView(String keyWords) {
        Log.v("copyed", "------copyed text:" + keyWords);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String BASE_URL = preferences.getString(PREF_SELECT_SEARCH_ENGINE, "");
        switch (BASE_URL) {
            case "Baidu":
                BASE_SEARCH_URL = BAIDU_BASE_SEARCH_URL;
                break;
            case "Google":
                BASE_SEARCH_URL = GOOGLE_BASE_SEARCH_URL;
                break;
            case "Bing":
                BASE_SEARCH_URL = BING_BASE_SEARCH_URL;
                break;
        }
        if (!TextUtils.isEmpty(keyWords)) {
            new FinestWebView.Builder(this)
                    .statusBarColorRes(R.color.colorPrimaryDark)
                    .show(BASE_SEARCH_URL + keyWords);
        }

    }

    private void createTranslateWindow() {
        wmParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        //wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.CENTER;
        wmParams.x = 100;
        wmParams.y = 100;

        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        root_layout = (RelativeLayout) inflater.inflate(R.layout.layout_widget_translate, null);
        mWindowManager.addView(root_layout, wmParams);
    }

    /**
     * 收起通知栏
     *
     * @param context
     */
    public static void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService(Context.USAGE_STATS_SERVICE);
            Method collapse;

            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }

    }
}