package com.shineyang.pullword_lib.parser;

import com.shineyang.pullword_lib.service.CallBack;

/**
 * Created by ShineYang on 2016/12/24.
 */
public interface Parser<T> {

    void parse(String text, CallBack<T> callback);

}
