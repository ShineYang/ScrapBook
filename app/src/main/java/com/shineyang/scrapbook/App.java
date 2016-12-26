package com.shineyang.scrapbook;

/**
 * Created by ShineYang on 2016/10/22.
 */

import android.app.Application;
import android.content.Context;

import com.shineyang.pullword_lib.action.CopyAction;
import com.shineyang.pullword_lib.action.SearchAction;
import com.shineyang.pullword_lib.action.ShareAction;
import com.shineyang.pullword_lib.bean.PullWord;
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
        setUpPullWordAction();
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

    public void setUpPullWordAction() {

        PullWord.registerAction(PullWord.ACTION_SEARCH, SearchAction.create());
        PullWord.registerAction(PullWord.ACTION_COPY, CopyAction.create());
        PullWord.registerAction(PullWord.ACTION_SHARE, ShareAction.create());
    }

}
