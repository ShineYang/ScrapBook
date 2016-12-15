package com.shineyang.scrapbook.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shineyang.scrapbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShineYang on 2016/10/29.
 */

public class MainContentRVViewHolder extends RecyclerView.ViewHolder {

    TextView tv_content;

    TextView tv_date;

    TextView tv_from;

    ImageView iv_like;

    private View mHeaderView;
    private View mFooterView;


    MainContentRVViewHolder(View itemView) {
        super(itemView);

        tv_content = (TextView) itemView.findViewById(R.id.tv_list_content);
        tv_from = (TextView) itemView.findViewById(R.id.tv_content_from);
        tv_date = (TextView) itemView.findViewById(R.id.tv_created_date);
        iv_like = (ImageView) itemView.findViewById(R.id.iv_main_list_like);

        if (itemView == mHeaderView) {
            return;
        }
        if (itemView == mFooterView) {
            return;
        }
    }

}
