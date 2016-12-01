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

import com.shineyang.scrapbook.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditorActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_editor)
    Toolbar toolbar_editor;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.edit_tool_bar_copy)
    RelativeLayout edit_tool_bar_copy;

    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        initToolBar();
        initEditToolBar();
        getExtraContent();
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

    public void initEditToolBar(){
        edit_tool_bar_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EditorActivity.this,FloatingWebViewActivity.class);
//                startActivity(intent);
            }
        });
    }

    public void getExtraContent() {
        Intent intent = getIntent();
        if (intent.hasExtra("list_content")) {
            content = intent.getStringExtra("list_content");
            edt_content.setText(content);
            edt_content.setSelection(content.length());//移动光标到最后
        }else{
            Log.v("editor","no content");
        }

        //隐藏软键盘
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
