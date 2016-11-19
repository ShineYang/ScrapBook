package com.shineyang.scrapbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shineyang.scrapbook.R;

/**
 * Created by ShineYang on 2016/11/19.
 */

public class AboutOpenSourceListAdapter extends BaseAdapter {
    private String[] itemTitle = {"ButterKnife", "GreenDAO", "Calligraphy"};
    private String[] itemContent = {
            "Field and method binding for Android views which uses annotation processing to generate boilerplate code for you.",
            "greenDAO is a light & fast ORM for Android that maps objects to SQLite databases. Being highly optimized for Android, greenDAO offers great performance and consumes minimal memory.",
            "Custom fonts in Android an OK way."};
    private LayoutInflater inflater;

    public AboutOpenSourceListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return itemTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_about_option_opensource_list, null);

            TextView tv_title = (TextView) convertView.findViewById(R.id.tv_about_opensource_proj_title);
            TextView tv_content = (TextView) convertView.findViewById(R.id.tv_about_opensource_proj_content);

            tv_title.setText(itemTitle[position]);
            tv_content.setText(itemContent[position]);
        }
        return convertView;
    }
}
