package com.shineyang.scrapbook.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ContentBean;

import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ContentBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig appBeanDaoConfig;
    private final DaoConfig contentBeanDaoConfig;

    private final AppBeanDao appBeanDao;
    private final ContentBeanDao contentBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        appBeanDaoConfig = daoConfigMap.get(AppBeanDao.class).clone();
        appBeanDaoConfig.initIdentityScope(type);

        contentBeanDaoConfig = daoConfigMap.get(ContentBeanDao.class).clone();
        contentBeanDaoConfig.initIdentityScope(type);

        appBeanDao = new AppBeanDao(appBeanDaoConfig, this);
        contentBeanDao = new ContentBeanDao(contentBeanDaoConfig, this);

        registerDao(AppBean.class, appBeanDao);
        registerDao(ContentBean.class, contentBeanDao);
    }
    
    public void clear() {
        appBeanDaoConfig.clearIdentityScope();
        contentBeanDaoConfig.clearIdentityScope();
    }

    public AppBeanDao getAppBeanDao() {
        return appBeanDao;
    }

    public ContentBeanDao getContentBeanDao() {
        return contentBeanDao;
    }

}
