package com.shineyang.scrapbook.action;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ShineYang on 2016/11/30.
 */

public class WidgetActionBridge extends IntentService {

    public final static int ACTION_SEARCH = 1;
    public final static String ACTION_CODE = "0";

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
        String action = intent.getStringExtra(Intent.EXTRA_TEXT);
        int actionCode = intent.getIntExtra(ACTION_CODE,0);
        Log.v("WidgetActionBridge", String.valueOf(actionCode));

        switch (actionCode) {
            case ACTION_SEARCH:
                showResultToast();
                break;
        }

    }

    public void showResultToast(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "点击测试", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
