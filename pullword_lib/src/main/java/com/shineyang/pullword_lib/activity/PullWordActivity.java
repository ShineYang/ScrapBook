package com.shineyang.pullword_lib.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.shineyang.pullword_lib.R;
import com.shineyang.pullword_lib.bean.PullWord;
import com.shineyang.pullword_lib.parser.HandlerCallback;
import com.shineyang.pullword_lib.parser.SimpleParser;
import com.shineyang.pullword_lib.view.PullWordLayout;

public class PullWordActivity extends AppCompatActivity implements PullWordLayout.ActionListener {

    public static final String EXTRA_TEXT = "extra_text";
    private PullWordLayout mLayout;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mLayout.reset();
        handleIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_word);
        initToolBar();
        mLayout = (PullWordLayout) findViewById(R.id.bigbang);
        mLayout.setActionListener(this);
        if (PullWord.getItemSpace() > 0) mLayout.setItemSpace(PullWord.getItemSpace());
        if (PullWord.getLineSpace() > 0) mLayout.setLineSpace(PullWord.getLineSpace());
        if (PullWord.getItemTextSize() > 0) mLayout.setItemTextSize(PullWord.getItemTextSize());
        handleIntent(getIntent());
    }

    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pullword);
        toolbar.setTitle(getResources().getString(R.string.text_pullword_activity_toolbar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void handleIntent(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String text = data.getQueryParameter(EXTRA_TEXT);

            if (TextUtils.isEmpty(text)) {
                finish();
                return;
            }

            SimpleParser parser = PullWord.getSegmentParser();
            parser.parse(text, new HandlerCallback<String[]>() {
                @Override
                public void onFinish(String[] result) {
                    mLayout.reset();
                    for (String str : result) {
                        mLayout.addTextItem(str);
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(PullWordActivity.this, "分词出错：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String selectedText = mLayout.getSelectedText();
        PullWord.startAction(this, PullWord.ACTION_BACK, selectedText);
    }

    @Override
    public void onSearch(String text) {
        PullWord.startAction(this, PullWord.ACTION_SEARCH, text);
    }

    @Override
    public void onShare(String text) {
        PullWord.startAction(this, PullWord.ACTION_SHARE, text);
    }

    @Override
    public void onCopy(String text) {
        PullWord.startAction(this, PullWord.ACTION_COPY, text);
    }

}