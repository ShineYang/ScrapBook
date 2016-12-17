package com.shineyang.scrapbook.adapter;

/**
 * Created by ShineYang on 2016/10/27.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.AppBeanDao;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;
import com.shineyang.scrapbook.utils.DBUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NaviListAdapter extends BaseAdapter {
    private static final int TYPE_FIXED_ITEM = 0;
    private static final int TYPE_APP_ITEM = 1;
    private Context context;
    private List<AppBean> beanList;
    private int defItem;//声明默认选中的项
    int[] appIcons = {R.mipmap.ic_all_list, R.mipmap.ic_favorite};
    String[] appNames = {"全部剪贴", "收藏"};
    String[] appItemCount = {"120", "32", "52", "11", "2", "11"};

    public NaviListAdapter(Context context, List<AppBean> beanList) {
        this.context = context;
        this.beanList = beanList;
        Log.v("beanlist", "------>size:" + String.valueOf(beanList.size()));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 1) {
            return TYPE_FIXED_ITEM;
        } else
            return TYPE_APP_ITEM;
    }

    @Override
    public int getCount() {
        if (beanList.size() == 0) {
            return 2;
        } else
            return beanList.size() + 2;
    }

    @Override
    public AppBean getItem(int position) {
        return beanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        NaviListViewHolder naviListViewHolder;
        if (convertView == null) {//重用视图但是convertView != null的时候 并没有去重新加载数据，写成下面这种形式就会重新加载数据了，解决了listview混乱问题
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            naviListViewHolder = new NaviListViewHolder();
            convertView = inflater.inflate(R.layout.layout_main_navi_list_item, null);
            convertView.setTag(naviListViewHolder);
        } else {
            naviListViewHolder = (NaviListViewHolder) convertView.getTag();
        }

        naviListViewHolder.iv_appIcon = (ImageView) convertView.findViewById(R.id.iv_navi_app_icon);
        naviListViewHolder.tv_appName = (TextView) convertView.findViewById(R.id.tv_navi_app_name);
        naviListViewHolder.tv_count = (TextView) convertView.findViewById(R.id.tv_navi_app_count);

        switch (type) {
            case TYPE_FIXED_ITEM:
                if (position == 0) {
                    naviListViewHolder.iv_appIcon.setImageResource(appIcons[position]);
                    naviListViewHolder.tv_appName.setText(appNames[position]);
                    naviListViewHolder.tv_count.setText(String.valueOf(getAllListCount()));
                } else {
                    naviListViewHolder.iv_appIcon.setImageResource(appIcons[position]);
                    naviListViewHolder.tv_appName.setText(appNames[position]);
                    naviListViewHolder.tv_count.setText("暂无");
                }
                break;
            case TYPE_APP_ITEM:
                naviListViewHolder.iv_appIcon.setImageBitmap(path2BitMap(beanList.get(position - 2).getAppIocnDir()));
                naviListViewHolder.tv_appName.setText(beanList.get(position - 2).getAppName());
                naviListViewHolder.tv_count.setText(DBUtils.readAppCopyedCount(beanList.get(position - 2).getAppName()));
                break;
        }

//        if (defItem == position) {
//            convertView.setBackgroundResource(R.drawable.selector_navi_list);
//        } else {
//            convertView.setBackgroundResource(android.R.color.transparent);
//        }

        return convertView;
    }

    public void readListFromDB(List<AppBean> appBeanList) {
        beanList = new ArrayList<>();
        Collections.reverse(appBeanList);
        beanList = appBeanList;
    }

    public String getSelectedAppName(int postion) {
        String appName;
        appName = beanList.get(postion - 2).getAppName();
        return appName;
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }

    private int getAllListCount() {
        int count;
        count = GreenDaoManager.getInstance().getSession().getListBeanDao().queryBuilder().list().size();
        return count;
    }

    //TO DO
    private int getAppCopyListCount(int postion) {
        int count = 0;
        return count;
    }

    private Bitmap path2BitMap(String path) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(path);
        return bitmap;
    }

    private class NaviListViewHolder {
        ImageView iv_appIcon;
        TextView tv_appName;
        TextView tv_count;
    }
}