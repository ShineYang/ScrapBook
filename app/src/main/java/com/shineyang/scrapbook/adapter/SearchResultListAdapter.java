package com.shineyang.scrapbook.adapter;

/**
 * Created by ShineYang on 2016/11/28.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;
import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.bean.SuggestionListBean;

import java.util.ArrayList;
import java.util.List;


public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.ViewHolder> {

    private List<SuggestionListBean> mDataSet = new ArrayList<>();
    private int mLastAnimatedItemPosition = -1;

    public SearchResultListAdapter(List<SuggestionListBean> suggestionListBeen){
        this.mDataSet = suggestionListBeen;
    }

    public interface OnItemClickListener{
        void onClick(SuggestionListBean suggestionListBean);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mColorName;
        public final View mTextContainer;

        public ViewHolder(View view) {
            super(view);
            mColorName = (TextView) view.findViewById(R.id.content_text);
            mTextContainer = view.findViewById(R.id.text_container);
        }
    }

    public void swapData(List<SuggestionListBean> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_result_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultListAdapter.ViewHolder holder, final int position) {

        SuggestionListBean listBean = mDataSet.get(position);

        holder.mColorName.setText(listBean.getBody());

        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        if(mItemsOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(mDataSet.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }
}