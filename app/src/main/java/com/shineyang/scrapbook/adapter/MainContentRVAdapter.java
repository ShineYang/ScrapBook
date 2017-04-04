package com.shineyang.scrapbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.bean.ContentBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ShineYang on 2016/10/29.
 */

public class MainContentRVAdapter extends RecyclerView.Adapter<MainContentRVViewHolder> {
    private static final int TYPE_HEADER = 0;  //说明是带有Header的
    private static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    private static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private Context context;
    public List<ContentBean> listDatas;

    private View mHeaderView;
    private View mFooterView;

    private OnRCVItemClickListener onRCVItemClickListener;

    public MainContentRVAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnRCVItemClickListener onRCVItemClickListener) {
        this.onRCVItemClickListener = onRCVItemClickListener;
    }

    public void onItemLikeClick(OnRCVItemClickListener onRCVItemLikeClickListener) {
        this.onRCVItemClickListener = onRCVItemLikeClickListener;
    }

    //FooterView的get和set函数

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public void readListData(List<ContentBean> listBeanContent) {
        listDatas = new ArrayList<>();
        Collections.reverse(listBeanContent);
        listDatas = listBeanContent;
    }

    public void addData(int postion, ContentBean contentBean) {
        //position为添加item的位置
        listDatas.add(postion, contentBean);
        notifyItemInserted(postion);
    }

    public void removeData(int position, ContentBean contentBean) {

    }

    public String getIdByPosition(int position) {
        return String.valueOf(listDatas.get(position).getId());
    }

    //创建新View，被LayoutManager所调用
    @Override
    public MainContentRVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_list_item, parent, false);

        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new MainContentRVViewHolder(mFooterView);
        } else
            return new MainContentRVViewHolder(view);
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键
     * 我们通过判断item的类型，从而绑定不同的view
     **/
    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final MainContentRVViewHolder holder, final int position) {

        if (getItemViewType(position) == TYPE_FOOTER) {
            return;
        }

        holder.tv_content.setText(listDatas.get(position).getContent());
        holder.tv_date.setText(listDatas.get(position).getDate());
        holder.tv_from.setText(listDatas.get(position).getFrom());

        if (listDatas.get(position).getIsCollect().equals("0")){
            holder.iv_like.setImageResource(R.drawable.ic_favorite_normal);
        }else holder.iv_like.setImageResource(R.drawable.ic_favorite);


        /**
         * ***判断是否设置了监听器***
         * 可以看到，这里实际上用到了子Item View的onClickListener和onLongClickListener这两个监听器，
         * 如果当前子item view被点击了，会触发点击事件进行回调，然后获取当前点击位置的position值，
         * 接着在②号代码处进行再次回调，而这一次的回调是我们自己手动添加的，需要实现上面所述的接口。
         */
        if (onRCVItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRCVItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition(), listDatas.get(position)); // 2
                }
            });

            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRCVItemClickListener.onItemLikeClick(holder.iv_like, holder.getAdapterPosition());
                    holder.iv_like.setImageResource(R.drawable.ic_favorite);
                }
            });
        }

    }

    //获取数据的数量
    @Override
    public int getItemCount() {

        if (mFooterView != null) {
            return listDatas.size() + 1;
        } else {
            return listDatas.size();
        }
    }

    public interface OnRCVItemClickListener {

        void onItemClick(View view, int position, ContentBean contentBean);

        void onItemLikeClick(ImageView iv_favorite, int position);
    }

}
