package com.shineyang.scrapbook.utils;

import android.util.Log;

import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShineYang on 2016/11/17.
 */

public class DBUtils {

    public static void addItem() {//插入剪贴项

    }

    public static void deleteItem() {//删除已有剪贴项

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

    public static void saveEditedContent() {//保存修改

    }

}
