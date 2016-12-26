package com.shineyang.pullword_lib.action;

/**
 * Created by ShineYang on 2016/12/26.
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by baoyongzhang on 2016/10/26.
 */
public class CopyAction implements Action {

    public static CopyAction create() {
        return new CopyAction();
    }

    @Override
    public void start(Context context, String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager service = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            service.setPrimaryClip(ClipData.newPlainText("BigBang", text));
            copySuccess(context);
        }
    }

    public void copySuccess(Context context) {
        Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show();
    }
}