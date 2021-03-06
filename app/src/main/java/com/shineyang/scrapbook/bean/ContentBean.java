package com.shineyang.scrapbook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by ShineYang on 2016/11/4.
 */
@Entity
public class ContentBean {
    @Id
    private Long id;
    private String content;
    private String from;
    private String date;
    private String isCollect;
    private boolean isSelect = false;

//    public ContentBean(String content, String from, String date, String isCollect) {
//        this.content = content;
//        this.from = from;
//        this.date = date;
//        this.isCollect = isCollect;
//    }

    public ContentBean(String content, String from, String date) {
        this.content = content;
        this.from = from;
        this.date = date;
        this.isCollect = "0";
    }


    public ContentBean() {
    }


    @Generated(hash = 1701810223)
    public ContentBean(Long id, String content, String from, String date, String isCollect,
            boolean isSelect) {
        this.id = id;
        this.content = content;
        this.from = from;
        this.date = date;
        this.isCollect = isCollect;
        this.isSelect = isSelect;
    }



    @Override
    public String toString() {
        return "listBean:\n" +
                "content:" + content.substring(0, 10) + "\n"
                + "from:" + from + "\n"
                + "date" + date + "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(String isCollect) {
        this.isCollect = isCollect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public boolean getIsSelect() {
        return this.isSelect;
    }


    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

}
