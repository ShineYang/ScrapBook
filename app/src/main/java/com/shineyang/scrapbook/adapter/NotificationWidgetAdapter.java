package com.shineyang.scrapbook.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.action.WidgetActionBridge;
import com.shineyang.scrapbook.bean.ListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ShineYang on 2016/11/30.
 */

public class NotificationWidgetAdapter {
    private RemoteViews expandedView;
    private List<ListBean> clipsList;
    private Context context;


    public NotificationWidgetAdapter(Context context, String clipedText) {//, ListBean clipObject
        this.context = context;

        String currentClip = clipedText;//显示在widget的内容
        clipsList = new ArrayList<>();
        expandedView = new RemoteViews(this.context.getPackageName(), R.layout.layout_notification_widget);
        expandedView.setTextViewText(R.id.tv_header_current_clip_text,currentClip);
        //search intent
        Intent openSearchIntent = new Intent(this.context, WidgetActionBridge.class)
                .putExtra(Intent.EXTRA_TEXT,currentClip)
                .putExtra(WidgetActionBridge.ACTION_CODE, WidgetActionBridge.ACTION_SEARCH);
        PendingIntent pOpenShareIntent = PendingIntent.getService(this.context,
                UUID.randomUUID().hashCode(),
                openSearchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.rl_widget_search, pOpenShareIntent);

        //sepatate intent
//        Intent openShareIntent = new Intent(this.context, ClipObjectActionBridge.class)
//                .putExtra(Intent.EXTRA_TEXT, currentClip)
//                .putExtra(ClipObjectActionBridge.ACTION_CODE, ClipObjectActionBridge.ACTION_SHARE);
//        PendingIntent pOpenShareIntent = PendingIntent.getService(this.context,
//                buttonNumber++,
//                openShareIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
        //expandedView.setOnClickPendingIntent(R.id.clip_share_button, pOpenShareIntent);

    }

    public RemoteViews build() {
        return expandedView;
    }

}
