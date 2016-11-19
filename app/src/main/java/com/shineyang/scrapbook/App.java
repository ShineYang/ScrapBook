package com.shineyang.scrapbook;

/**
 * Created by ShineYang on 2016/10/22.
 */

import android.app.Application;
import android.content.Context;

import com.shineyang.scrapbook.greendao.GreenDaoManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    private static Context mContext;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        if (instance == null) {
            instance = this;
        }
//设置字体
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Euphemia.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        GreenDaoManager.getInstance();
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }


}
