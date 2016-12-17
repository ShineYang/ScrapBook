package com.shineyang.scrapbook.utils;

import android.util.Log;

import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShineYang on 2016/11/17.
 */

public class DBUtils {

    private static ListBeanDao getListBeanDao() {
        return GreenDaoManager.getInstance().getSession().getListBeanDao();
    }

    private static AppBeanDao getAppBeanDao() {
        return GreenDaoManager.getInstance().getSession().getAppBeanDao();
    }

    public static void addItem(ListBean newContent) {
        GreenDaoManager.getInstance().getSession().getListBeanDao().insert(newContent);
    }

    public static void deleteItem() {

    }

    public static List<ListBean> getBeanListByAppName(String appName) {
        List<ListBean> list;
        list = getListBeanDao().queryBuilder().where(
                ListBeanDao.Properties.From.eq(appName)
        ).list();
        return list;
    }

    public static String readAppCopyedCount(String appName) {
        int count;
        count = getListBeanDao().queryBuilder()
                .where(ListBeanDao.Properties.From.eq(appName))
                .build().list().size();
        return String.valueOf(count);
    }

    public static List<AppBean> readNavigationList() {
        List<AppBean> naviList;
        naviList = getAppBeanDao().queryBuilder().list();
        return naviList;
    }

    public static List<ListBean> readAllList() {
        List<ListBean> allList;
        allList = getListBeanDao().queryBuilder().list();
        return allList;
    }

    public static List<ListBean> queryItem(String searchContent) {//search
        List<ListBean> searchResult;
        searchResult = getListBeanDao().queryBuilder()
                .where(ListBeanDao.Properties.Content.like("%" + searchContent + "%"))
                .build().list();
        return searchResult;
    }

    public static List<ListBean> readTop10ListBean() {
        List<ListBean> listData = new ArrayList<>();
        listData = getListBeanDao().queryBuilder()
                .limit(10).list();
        return listData;
    }

    public static void saveEditedContent(String id, String newContent) {//save
        ListBeanDao listBeanDao = getListBeanDao();
        List<ListBean> list = listBeanDao.queryBuilder().where(
                ListBeanDao.Properties.Id.eq(id)
        ).build().list();
        for (ListBean bean : list) {
            bean.setContent(newContent);
            listBeanDao.insertOrReplaceInTx(bean);
        }

    }

}
