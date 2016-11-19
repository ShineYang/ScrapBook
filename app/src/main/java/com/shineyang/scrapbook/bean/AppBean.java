package com.shineyang.scrapbook.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ShineYang on 2016/11/10.
 */

@Entity
public class AppBean {
    private String appName;
    private String appPackageName;
    private String appIocnDir;
    private int listSize;

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public AppBean(String appName, String appPackageName, String appIocnDir) {
        this.appName = appName;
        this.appPackageName = appPackageName;
        this.appIocnDir = appIocnDir;
    }

    @Generated(hash = 76008672)
    public AppBean(String appName, String appPackageName, String appIocnDir,
            int listSize) {
        this.appName = appName;
        this.appPackageName = appPackageName;
        this.appIocnDir = appIocnDir;
        this.listSize = listSize;
    }

    @Generated(hash = 285800313)
    public AppBean() {
    }

    public String getAppIocnDir() {
        return appIocnDir;
    }

    public void setAppIocnDir(String appIocnDir) {
        this.appIocnDir = appIocnDir;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }


}
