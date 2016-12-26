package com.shineyang.pullword_lib.service;

/**
 * Created by ShineYang on 2016/12/24.
 */

public interface CallBack<T> {
    void finish(T result);
    void error(Exception e);
}
