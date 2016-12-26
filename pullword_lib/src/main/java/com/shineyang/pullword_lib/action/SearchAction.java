package com.shineyang.pullword_lib.action;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.shineyang.pullword_lib.R;
import com.thefinestartist.finestwebview.FinestWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ShineYang on 2016/12/24.
 */

public class SearchAction implements Action {
    public static String BAIDU_BASE_SEARCH_URL = "https://www.baidu.com/s?wd=";

    public static SearchAction create() {
        return new SearchAction();
    }

    @Override
    public void start(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {

            new FinestWebView.Builder(context)
                    .statusBarColorRes(R.color.colorPrimaryDark)
                    .show(BAIDU_BASE_SEARCH_URL + text);
        }
    }

}