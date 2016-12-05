package com.shineyang.scrapbook.action;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.shineyang.scrapbook.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.lang.reflect.Method;

/**
 * Created by ShineYang on 2016/11/30.
 */

public class WidgetActionBridge extends IntentService {

    public final static int ACTION_SEARCH = 1;
    public final static int ACTION_SEPARATE = 2;
    public final static int ACTION_TRANSLATE = 3;
    public final static int ACTION_QR_CODE = 4;
    public final static int ACTION_SHARE = 5;
    public final static String ACTION_CODE = "0";

    public static String BAIDU_SEARCH_BASE_URL = "https://www.baidu.com/s?wd=";


    private Intent intent;
    public Handler handler;

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
            case ACTION_SEPARATE:
                break;
            case ACTION_TRANSLATE:
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

    public void separateClipedContent() {

    }

    public void translateClipedContent() {

    }

    public void createQRCode() {

    }

    public void searchWithWebView(String keyWords) {
        new FinestWebView.Builder(this)
                .statusBarColorRes(R.color.colorPrimaryDark)
                .show(BAIDU_SEARCH_BASE_URL + keyWords);
    }

}
