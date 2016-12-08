package com.shineyang.scrapbook.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.utils.EditTextUtil;
import com.shineyang.scrapbook.view.EditableToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_editor)
    Toolbar toolbar_editor;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.included_edit_tool_bar)
    EditableToolBar editableToolBar;

    private String content;
    private EditTextUtil editTextUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        initToolBar();
        getExtraContent();
        initEditToolBar();
    }

    public void initToolBar() {
        toolbar_editor.setTitle("文字编辑");
        setSupportActionBar(toolbar_editor);
        toolbar_editor.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initEditToolBar() {
        editTextUtil = new EditTextUtil(edt_content);
        editableToolBar.setOnUndoClickListener(new EditableToolBar.OnUndoClickListener() {
            @Override
            public void onUndoClick() {
                editTextUtil.undo();
            }
        });

        editableToolBar.setOnRedoClickListener(new EditableToolBar.OnRedoClickListener() {
            @Override
            public void onRedoClick() {
                editTextUtil.redo();
            }
        });

        editableToolBar.setOnSeparateClickListener(new EditableToolBar.OnSeparateClickListener() {
            @Override
            public void onSeparateClick() {

            }
        });

        editableToolBar.setOnCopyClickListener(new EditableToolBar.OnCopyClickListener() {
            @Override
            public void onCopyClick() {

            }
        });
    }

    public void getExtraContent() {
        Intent intent = getIntent();
        if (intent.hasExtra("list_content")) {
            content = intent.getStringExtra("list_content");
            edt_content.setText(content);
            edt_content.setSelection(content.length());//移动光标到最后
        } else {
            Log.v("editor", "no content");
        }

        //隐藏软键盘
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空操作栈
        editTextUtil.clearHistory();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
