package com.shineyang.scrapbook.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shineyang.scrapbook.R;

import butterknife.BindView;

/**
 * Created by ShineYang on 2016/12/8.
 */

public class EditableToolBar extends LinearLayout {

    private OnUndoClickListener onUndoClickListener = null;
    private OnRedoClickListener onRedoClickListener = null;
    private OnPullWordClickListener onPullWordClickListener = null;
    private OnCopyClickListener onCopyClickListener = null;
    private OnShareClickListener onShareClickListener =null;

    public EditableToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_editable_tool_bar, this, true);
        initEditableToolBarBtn();
    }

    public void initEditableToolBarBtn() {
        RelativeLayout rl_undo_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_undo);
        RelativeLayout rl_redo_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_redo);
        RelativeLayout rl_pullword_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_pullword);
        RelativeLayout rl_copy_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_copy);
        RelativeLayout rl_share_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_share);

        rl_undo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onUndoClickListener.onUndoClick();
            }
        });

        rl_redo_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onRedoClickListener.onRedoClick();
            }
        });

        rl_pullword_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPullWordClickListener.onPullWordClick();
            }
        });

        rl_copy_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCopyClickListener.onCopyClick();
            }
        });

        rl_share_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareClickListener.onShareClick();
            }
        });


    }

    public void setOnUndoClickListener(OnUndoClickListener onUndoClickListener) {
        this.onUndoClickListener = onUndoClickListener;
    }

    public void setOnRedoClickListener(OnRedoClickListener onRedoClickListener) {
        this.onRedoClickListener = onRedoClickListener;
    }

    public void setOnPullWordClickListener(OnPullWordClickListener onPullWordClickListener) {
        this.onPullWordClickListener = onPullWordClickListener;
    }

    public void setOnCopyClickListener(OnCopyClickListener onCopyClickListener) {
        this.onCopyClickListener = onCopyClickListener;
    }

    public void setOnShareClickListener(OnShareClickListener onShareClickListener) {
        this.onShareClickListener = onShareClickListener;
    }



    public interface OnUndoClickListener {
        void onUndoClick();
    }

    public interface OnRedoClickListener {
        void onRedoClick();
    }

    public interface OnPullWordClickListener {
        void onPullWordClick();
    }

    public interface OnCopyClickListener {
        void onCopyClick();
    }

    public interface OnShareClickListener {
        void onShareClick();
    }
}
