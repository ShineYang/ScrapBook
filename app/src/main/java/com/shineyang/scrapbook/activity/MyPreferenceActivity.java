package com.shineyang.scrapbook.activity;

/**
 * Created by ShineYang on 2016/12/21.
 */

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.shineyang.scrapbook.R;

/**
 * Created by heruoxin on 15/2/28.
 */
public class MyPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        toolbar.setTitle("设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setDivider(getResources().getDrawable(R.drawable.setting_divider));
    }

    //Fix LG support V7 bug:
    //https://code.google.com/p/android/issues/detail?id=78154
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && "LGE".equalsIgnoreCase(Build.BRAND)) {
            openOptionsMenu();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MyActionBarActivity.ACTIVITY_CLOSED));
        super.onPause();
    }

    @Override
    protected void onResume() {
        //LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MyActionBarActivity.ACTIVITY_OPENED));
        super.onResume();
    }
}
