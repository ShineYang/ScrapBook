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

/**
 * Created by ShineYang on 2016/12/8.
 */

public class EditableToolBar extends LinearLayout {

    private RelativeLayout rl_undo_btn;
    private RelativeLayout rl_redo_btn;
    private RelativeLayout rl_separate_btn;
    private RelativeLayout rl_copy_btn;

    private OnUndoClickListener onUndoClickListener = null;
    private OnRedoClickListener onRedoClickListener = null;
    private OnSeparateClickListener onSeparateClickListener = null;
    private OnCopyClickListener onCopyClickListener = null;

    public EditableToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_editable_tool_bar, this, true);
        initEditableToolBarBtn();
    }

    public void initEditableToolBarBtn() {
        rl_undo_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_undo);
        rl_redo_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_redo);
        rl_separate_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_separate);
        rl_copy_btn = (RelativeLayout) findViewById(R.id.edit_tool_bar_copy);

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

        rl_separate_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSeparateClickListener.onSeparateClick();
            }
        });

        rl_copy_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCopyClickListener.onCopyClick();
            }
        });


    }

//    public void setOnEditableItemClickListener(OnEditableItemClickListener onEditableItemClickListener) {
//        this.onEditableItemClickListener = onEditableItemClickListener;
//    }

    public void setOnUndoClickListener(OnUndoClickListener onUndoClickListener) {
        this.onUndoClickListener = onUndoClickListener;
    }

    public void setOnRedoClickListener(OnRedoClickListener onRedoClickListener) {
        this.onRedoClickListener = onRedoClickListener;
    }

    public void setOnSeparateClickListener(OnSeparateClickListener onSeparateClickListener) {
        this.onSeparateClickListener = onSeparateClickListener;
    }

    public void setOnCopyClickListener(OnCopyClickListener onCopyClickListener) {
        this.onCopyClickListener = onCopyClickListener;
    }


    public interface OnUndoClickListener {
        void onUndoClick();
    }

    public interface OnRedoClickListener {
        void onRedoClick();
    }

    public interface OnSeparateClickListener {
        void onSeparateClick();
    }

    public interface OnCopyClickListener {
        void onCopyClick();
    }

}
