package com.shineyang.scrapbook.greendao;

import com.shineyang.scrapbook.App;
import com.shineyang.scrapbook.greendao.gen.DaoMaster;
import com.shineyang.scrapbook.greendao.gen.DaoSession;

/**
 * Created by ShineYang on 2016/11/6.
 */

public class GreenDaoManager {
    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    public GreenDaoManager() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(App.getContext(), "notes-db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
