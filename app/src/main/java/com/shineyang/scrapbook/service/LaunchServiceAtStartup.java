package com.shineyang.scrapbook.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ShineYang on 2016/12/8.
 */

public class LaunchServiceAtStartup extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            CBWatcherService.startCBService(context);

        }
    }
}
