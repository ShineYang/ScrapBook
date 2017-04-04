package com.shineyang.scrapbook.utils;

import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ContentBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ContentBeanDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShineYang on 2016/11/17.
 */

public class DBUtils {

    private static ContentBeanDao getContentBeanDao() {
        return GreenDaoManager.getInstance().getSession().getContentBeanDao();
    }

    private static AppBeanDao getAppBeanDao() {
        return GreenDaoManager.getInstance().getSession().getAppBeanDao();
    }

    public static void addItem(ContentBean newContent) {
        GreenDaoManager.getInstance().getSession().getContentBeanDao().insert(newContent);
    }

    public static void deleteItem() {

    }

    public static List<ContentBean> getContentBeanById(String id){
        List<ContentBean> bean = getContentBeanDao().queryBuilder().where(
                ContentBeanDao.Properties.Id.eq(id)
        ).build().list();
        return bean;
    }

    public static List<ContentBean> getBeanListByAppName(String appName) {
        List<ContentBean> list;
        list = getContentBeanDao().queryBuilder().where(
                ContentBeanDao.Properties.From.eq(appName)
        ).list();
        return list;
    }

    public static String readAppCopyedCount(String appName) {
        int count;
        count = getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.From.eq(appName))
                .build().list().size();
        return String.valueOf(count);
    }

    public static Boolean isfavoriteItem(String id) {
        Boolean isfavoriteItem = false;
        ContentBeanDao listBeanDao = getContentBeanDao();
        List<ContentBean> list = listBeanDao.queryBuilder().where(
                ContentBeanDao.Properties.Id.eq(id)
        ).build().list();
        for (ContentBean bean : list) {
            if (bean.getIsCollect().equals("0")) {
                isfavoriteItem = false;
            } else {
                isfavoriteItem = true;
            }
            listBeanDao.insertOrReplaceInTx(bean);
        }
        return isfavoriteItem;
    }

    public static void starItem(String id) {
        ContentBeanDao listBeanDao = getContentBeanDao();
        List<ContentBean> list = listBeanDao.queryBuilder().where(
                ContentBeanDao.Properties.Id.eq(id)
        ).build().list();

        for (ContentBean bean : list) {
            if (bean.getIsCollect().equals("0")) {
                bean.setIsCollect("1");
            } else {
                bean.setIsCollect("0");
            }
            listBeanDao.insertOrReplaceInTx(bean);
        }
    }


    public static List<AppBean> readNavigationList() {
        List<AppBean> naviList;
        naviList = getAppBeanDao().queryBuilder().list();
        return naviList;
    }

    public static List<ContentBean> readAllList() {
        List<ContentBean> allList;
        allList = getContentBeanDao().queryBuilder().list();
        return allList;
    }

    public static List<ContentBean> readFavoriteList() {
        List<ContentBean> favoriteList;
        favoriteList = getContentBeanDao().queryBuilder().where(
                ContentBeanDao.Properties.IsCollect.eq("1")
        ).list();
        return favoriteList;
    }

    public static int getAllListCount() {
        int count;
        count = getContentBeanDao().queryBuilder()
                .list().size();
        return count;
    }

    public static int getCollectedListCount() {
        int count;
        count = getContentBeanDao().queryBuilder().where(
                ContentBeanDao.Properties.IsCollect.eq("1")
        ).list().size();
        return count;
    }

    public static List<ContentBean> queryItem(String searchContent) {//search
        List<ContentBean> searchResult;
        searchResult = getContentBeanDao().queryBuilder()
                .where(ContentBeanDao.Properties.Content.like("%" + searchContent + "%"))
                .build().list();
        return searchResult;
    }

    public static List<ContentBean> readTop10ListBean() {
        List<ContentBean> listData = new ArrayList<>();
        listData = getContentBeanDao().queryBuilder()
                .limit(10).list();
        return listData;
    }

    public static void saveEditedContent(String id, String newContent) {//save
        ContentBeanDao listBeanDao = getContentBeanDao();
        List<ContentBean> list = listBeanDao.queryBuilder().where(
                ContentBeanDao.Properties.Id.eq(id)
        ).build().list();
        for (ContentBean bean : list) {
            bean.setContent(newContent);
            listBeanDao.insertOrReplaceInTx(bean);
        }

    }

}
