package com.shineyang.scrapbook.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.adapter.AboutOpenSourceListAdapter;
import com.shineyang.scrapbook.adapter.AboutOptionAdapter;
import com.shineyang.scrapbook.view.BetterListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_about)
    Toolbar toolbar;
    @BindView(R.id.lv_about_option)
    BetterListView lv_about_option;
    @BindView(R.id.lv_about_opensource)
    BetterListView lv_opensource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initToolBar();
        initAboutOptionList();
    }

    public void initToolBar() {
        toolbar.setTitle(getResources().getString(R.string.about_text));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initAboutOptionList() {
        AboutOptionAdapter aboutOptionAdapter = new AboutOptionAdapter(this);
        lv_about_option.setAdapter(aboutOptionAdapter);

        AboutOpenSourceListAdapter aboutOpenSourceListAdapter = new AboutOpenSourceListAdapter(this);
        lv_opensource.setAdapter(aboutOpenSourceListAdapter);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
