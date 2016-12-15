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

    public static void addItem(ListBean newContent) {
        GreenDaoManager.getInstance().getSession().getListBeanDao().insert(newContent);
    }

    public static void deleteItem() {

    }

    public static String readAppCopyedCount(String appName) {
        int count;
        count = GreenDaoManager.getInstance().getSession().getListBeanDao().queryBuilder()
                .where(ListBeanDao.Properties.From.eq(appName))
                .build().list().size();
        return String.valueOf(count);
    }

    public static List<AppBean> readNavigationList() {
        List<AppBean> naviList;
        naviList = GreenDaoManager.getInstance().getSession().getAppBeanDao().queryBuilder().list();
        return naviList;
    }

    public static List<ListBean> readAllList() {
        List<ListBean> allList;
        allList = GreenDaoManager.getInstance().getSession().getListBeanDao().queryBuilder().list();
        return allList;
    }

    public static List<ListBean> queryItem(String searchContent) {//查找剪贴项
        List<ListBean> searchResult;
        searchResult = GreenDaoManager.getInstance().getSession().getListBeanDao().queryBuilder()
                .where(ListBeanDao.Properties.Content.like("%" + searchContent + "%"))
                .build().list();
        return searchResult;
    }

    public static List<ListBean> readTop10ListBean() {//读取最新10个剪贴数据
        List<ListBean> listData = new ArrayList<>();
        listData = GreenDaoManager.getInstance().getSession().getListBeanDao().queryBuilder()
                .limit(10).list();
        return listData;
    }

    public static void saveEditedContent(String id, String newContent) {//保存修改
        ListBeanDao listBeanDao = GreenDaoManager.getInstance().getSession().getListBeanDao();
        List<ListBean> list = listBeanDao.queryBuilder().where(
                ListBeanDao.Properties.Id.eq(id)
        ).build().list();
        for (ListBean bean : list) {
            bean.setContent(newContent);
            listBeanDao.insertOrReplaceInTx(bean);
        }

    }

}
