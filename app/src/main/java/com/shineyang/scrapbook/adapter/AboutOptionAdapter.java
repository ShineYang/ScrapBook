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

public class AboutOptionAdapter extends BaseAdapter {

    private String[] itemText = {"介绍", "反馈", "代码", "开源库"};
    private int[] itemIcons = {R.mipmap.ic_about_option_about,
            R.mipmap.ic_about_option_feedback,
            R.mipmap.ic_about_github,
            R.mipmap.ic_about_option_sourcecode};
    private LayoutInflater inflater;

    public AboutOptionAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemIcons.length;
    }

    @Override
    public Object getItem(int position) {
        return itemIcons[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_about_option_list_item, null);

            TextView tv = (TextView) convertView.findViewById(R.id.tv_option_title);
            ImageView iv = (ImageView) convertView.findViewById(R.id.iv_option_icon);

            iv.setImageResource(itemIcons[position]);
            tv.setText(itemText[position]);
        }
        return convertView;
    }
}
