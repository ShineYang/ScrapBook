package com.shineyang.pullword_lib.parser;

/**
 * Created by ShineYang on 2016/12/24.
 */

import android.os.Handler;

import com.shineyang.pullword_lib.service.CallBack;

/**
 * Created by baoyongzhang on 2016/10/27.
 */
public abstract class HandlerCallback<T> implements CallBack<T> {

    private final Handler mHandler;

    public HandlerCallback() {
        mHandler = new Handler();
    }

    @Override
    public void finish(final T result) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFinish(result);
            }
        });
    }

    @Override
    public void error(final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onError(e);
            }
        });
    }

    public abstract void onFinish(T result);
    public abstract void onError(Exception e);
}
