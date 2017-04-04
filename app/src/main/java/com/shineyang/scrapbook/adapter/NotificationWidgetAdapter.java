package com.shineyang.scrapbook.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.action.WidgetActionBridge;
import com.shineyang.scrapbook.bean.ContentBean;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ShineYang on 2016/11/30.
 */

public class NotificationWidgetAdapter {
    private RemoteViews expandedView;
    private List<ContentBean> clipsList;
    private Context context;


    public NotificationWidgetAdapter(Context context, String clipedText) {//, ContentBean clipObject
        this.context = context;
        clipsList = new ArrayList<>();
        expandedView = new RemoteViews(this.context.getPackageName(), R.layout.layout_notification_widget);
        expandedView.setTextViewText(R.id.tv_header_current_clip_text, clipedText);
        //search intent
        Intent openSearchIntent = new Intent(this.context, WidgetActionBridge.class)
                .putExtra(Intent.EXTRA_TEXT, clipedText)
                .putExtra(WidgetActionBridge.ACTION_CODE, WidgetActionBridge.ACTION_SEARCH);

        PendingIntent pOpenShareIntent = PendingIntent.getService(this.context,
                UUID.randomUUID().hashCode(),
                openSearchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.rl_widget_search, pOpenShareIntent);

        //translate intent
        Intent openTranslateIntent = new Intent(this.context, WidgetActionBridge.class)
                .putExtra(Intent.EXTRA_TEXT, clipedText)
                .putExtra(WidgetActionBridge.ACTION_CODE, WidgetActionBridge.ACTION_TRANSLATE);
        PendingIntent pOpenopenTranslateIntent = PendingIntent.getService(this.context,
                UUID.randomUUID().hashCode(),
                openTranslateIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.rl_widget_translate, pOpenopenTranslateIntent);

        //pullword intent
        Intent openPullWordIntent = new Intent(this.context, WidgetActionBridge.class)
                .putExtra(Intent.EXTRA_TEXT, clipedText)
                .putExtra(WidgetActionBridge.ACTION_CODE, WidgetActionBridge.ACTION_PULLWORD);
        PendingIntent pOpenopenPullWordIntent = PendingIntent.getService(this.context,
                UUID.randomUUID().hashCode(),
                openPullWordIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        expandedView.setOnClickPendingIntent(R.id.rl_widget_pullword, pOpenopenPullWordIntent);

    }

    public RemoteViews build() {
        return expandedView;
    }

}
