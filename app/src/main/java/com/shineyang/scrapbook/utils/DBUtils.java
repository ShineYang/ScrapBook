package com.shineyang.scrapbook.utils;

import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;

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

    public static void readMainListContent() {//读取剪贴列表

    }

    public static void saveEditedContent() {//保存修改

    }

}
