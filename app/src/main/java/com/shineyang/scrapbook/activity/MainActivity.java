package com.shineyang.scrapbook.activity;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jaeger.library.StatusBarUtil;
import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.adapter.MainContentRVAdapter;
import com.shineyang.scrapbook.adapter.NaviListAdapter;
import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ContentBean;
import com.shineyang.scrapbook.service.CBWatcherService;
import com.shineyang.scrapbook.utils.ActivityAnimUtil;
import com.shineyang.scrapbook.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_main)
    Toolbar toolbar_main;
    @BindView(R.id.drawer_list)
    ListView drawer_list;
    @BindView(R.id.drawerlayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.lv_main_content)
    RecyclerView rv_main_content;
    @BindView(R.id.iv_navi_header_icon)
    ImageView iv_navi_header_icon;
    @BindView(R.id.fab_add)
    FloatingActionButton fab_add;
    @BindView(R.id.rl_no_content)
    RelativeLayout rl_no_content;

    private Context context;

    public final static String PREF_START_SERVICE = "open_service_switch";

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private List<ContentBean> listData;

    private NaviListAdapter naviListAdapter;

    private MainContentRVAdapter mainContentRVAdapter = null;

    private MaterialDialog dialog;

    private int itemType;

    private static int drawerItemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = getApplicationContext();
        initToolBar();
        initNaivigationView();
        initMainList();
        initFloatActionBar();
        startService();
    }

    public void startService() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (!preferences.getBoolean(PREF_START_SERVICE, true)) {
            context.stopService(new Intent(context, CBWatcherService.class));
        } else
            context.startService(new Intent(context, CBWatcherService.class));

        //context.startService(new Intent(context, FloatingWindowService.class));
        Log.v("floating","----------start");
    }

    public void initToolBar() {
        toolbar_main.setTitle(getResources().getString(R.string.text_main_toolbar_all));
        setSupportActionBar(toolbar_main);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            StatusBarUtil.setTranslucentForDrawerLayout(this, drawerLayout, 55);
        }

    }

    public void initNaivigationView() {
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_main, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);
        initNaviList();

        iv_navi_header_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//ABOUT
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }


    public void initNaviList() {
        readNaviBeanList();
        naviListAdapter = new NaviListAdapter(this, readNaviBeanList());
        naviListAdapter.setDefSelect(0);
        drawer_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        drawer_list.setAdapter(naviListAdapter);
        drawer_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //naviListAdapter.setDefSelect(i);//refresh selected row.
                drawerLayout.closeDrawers();
                drawerItemPosition = i;
                itemType = naviListAdapter.getItemType(i);
                loadContentByCategory(i);
            }
        });
    }

    public void loadContentByCategory(int itemPosition) {
        switch (itemType) {
            case 0:
                if (itemPosition == 0) {
                    toolbar_main.setTitle(getResources().getString(R.string.text_main_toolbar_all));
                    reLoadMainContentList();
                } else {
                    setToolBarTitle(getResources().getString(R.string.text_favorite));
                    loadFavoriate();
                }
                break;
            case 1:
                String appName = naviListAdapter.getSelectedAppName(itemPosition);
                setToolBarTitle(appName);
                loadDataByAppName(appName);
                break;
        }
    }

    public void setToolBarTitle(String title) {
        toolbar_main.setTitle(getResources().getString(R.string.text_toolbar_from) + title);
    }

    public void loadDataByAppName(final String appName) {
        List<ContentBean> newListBeen = DBUtils.getBeanListByAppName(appName);
        rv_main_content.removeAllViews();
        mainContentRVAdapter.readListData(newListBeen);
        rv_main_content.setAdapter(mainContentRVAdapter);
    }

    public void loadFavoriate() {
        List<ContentBean> favoriteListBeen;
        if (DBUtils.readFavoriteList().size() != 0) {
            favoriteListBeen = DBUtils.readFavoriteList();
            rv_main_content.removeAllViews();
            mainContentRVAdapter.readListData(favoriteListBeen);
            rv_main_content.setAdapter(mainContentRVAdapter);
        } else
            Toast.makeText(getApplicationContext(), "暂无收藏", Toast.LENGTH_SHORT).show();

    }

    public void initMainList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_main_content.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rv_main_content.setHasFixedSize(true);
        mainContentRVAdapter = new MainContentRVAdapter(this);

        if (readListContent().size() == 0) {
            rl_no_content.setVisibility(View.VISIBLE);
            rv_main_content.setVisibility(View.GONE);
        } else {
            rv_main_content.setVisibility(View.VISIBLE);
            mainContentRVAdapter.readListData(readListContent());
            //set footer
            View footer = LayoutInflater.from(this).inflate(R.layout.layout_main_list_footer, rv_main_content, false);
            mainContentRVAdapter.setFooterView(footer);
            rv_main_content.setAdapter(mainContentRVAdapter);

            mainContentRVAdapter.setOnItemClickListener(new MainContentRVAdapter.OnRCVItemClickListener() {
                @Override
                public void onItemClick(View view, int position, ContentBean contentBean) {
                    Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                    intent.putExtra("list_id", String.valueOf(contentBean.getId()))
                            .putExtra("list_content", contentBean.getContent())
                            .putExtra("list_from", contentBean.getFrom())
                            .putExtra("list_date", contentBean.getDate());
                    startActivity(intent);
                }

                @Override
                public void onItemLikeClick(ImageView iv_favorite, int position) {
                    Boolean isfavorite;
                    String id = mainContentRVAdapter.getIdByPosition(position);
                    DBUtils.starItem(id);
                    isfavorite = DBUtils.isfavoriteItem(id);
                    if (!isfavorite) {
                        iv_favorite.setImageResource(R.drawable.ic_favorite_normal);
                        mainContentRVAdapter.notifyItemChanged(position);
                        Toast.makeText(getApplicationContext(), "取消收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        iv_favorite.setImageResource(R.drawable.ic_favorite);
                        Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    }
                    reLoadNaviList();

                }
            });
        }

    }


    public void initFloatActionBar() {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mainContentRVAdapter.addData(0, insertListContent("数据库插入的第" + readListContent().size() + "条", "scrapbook", "2016.11.07"));
                //rv_main_content.scrollToPosition(0);//回滚到头部
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("addMode", true);
                ActivityAnimUtil.startActivity(MainActivity.this, intent, view, R.color.white);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


    public List<ContentBean> readListContent() {
        listData = new ArrayList<>();
        listData = DBUtils.readAllList();
        return listData;
    }

    public List<AppBean> readNaviBeanList() {
        List<AppBean> appBeanList;
        appBeanList = DBUtils.readNavigationList();
        return appBeanList;
    }

    public void showProgressDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.text_progress_dialog)
                .content(R.string.text_progress_dialog_content)
                .progress(true, 0)
                .show();
    }

    public void dissmissProgressDialog() {
        dialog.dismiss();
    }

    public void reLoadMainContentList() {
        toolbar_main.setTitle(getResources().getString(R.string.text_main_toolbar_all));
        mainContentRVAdapter.readListData(readListContent());
        rv_main_content.setAdapter(mainContentRVAdapter);
    }

    public void reLoadNaviList() {
        naviListAdapter.readListFromDB(readNaviBeanList());
        naviListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reLoadNaviList();
        //reLoadMainContentList();
        loadContentByCategory(drawerItemPosition);
        /**
         * TODO
         * 在进入分类加载主页列表状态下
         * 在回退到MainActivity时,不执行加载所有列表，而是刷新当前分类下列表
         * 需要写方法实现按条件加载内容
         */
    }

    private void checkUsagePermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode;
            mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            Log.v("granted", "--------" + String.valueOf(granted));
            if (!granted) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);

        final MenuItem item_search = menu.findItem(R.id.action_search);
        final MenuItem item_permission = menu.findItem(R.id.action_open_permission);
        final MenuItem item_setting = menu.findItem(R.id.action_setting);
        final MenuItem item_about = menu.findItem(R.id.action_about);

        item_search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });

        item_permission.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });

        item_setting.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return false;
            }
        });

        item_about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return false;
            }
        });


        return true;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
