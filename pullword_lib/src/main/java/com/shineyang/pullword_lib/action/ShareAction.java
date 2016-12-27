package com.shineyang.pullword_lib.action;

/**
 * Created by ShineYang on 2016/12/26.
 */

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class ShareAction implements Action {

    public static ShareAction create() {
        return new ShareAction();
    }

    @Override
    public void start(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(sharingIntent, "分享到"));
        }
    }
}
