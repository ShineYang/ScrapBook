package com.shineyang.scrapbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
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
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.adapter.MainContentRVAdapter;
import com.shineyang.scrapbook.adapter.NaviListAdapter;
import com.shineyang.scrapbook.bean.AppBean;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.greendao.GreenDaoManager;
import com.shineyang.scrapbook.greendao.gen.ListBeanDao;
import com.shineyang.scrapbook.service.CBWatcherService;
import com.shineyang.scrapbook.utils.ActivityAnimUtil;
import com.shineyang.scrapbook.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
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

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private List<ListBean> listData;

    private NaviListAdapter naviListAdapter;

    private MainContentRVAdapter mainContentRVAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();
        initNaivigationView();
        initMainList();
        initFloatActionBar();
        CBWatcherService.startCBService(getApplicationContext());
    }


    public void initToolBar() {
        toolbar_main.setTitle("全部剪贴");
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
                naviListAdapter.setDefSelect(i);//刷新选中行
                drawerLayout.closeDrawers();
            }
        });
    }

    public int setFavoriteIcon(int type) {
        if (type == 0) {
            return R.drawable.ic_favorite_normal;
        } else
            return R.drawable.ic_favorite;
    }

    public void initMainList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_main_content.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        rv_main_content.setHasFixedSize(true);

        //recyclerview-animators
        //rv_main_content.setItemAnimator(new SlideInLeftAnimator());

        mainContentRVAdapter = new MainContentRVAdapter(this);

        if (readListContent().size() == 0) {
            rl_no_content.setVisibility(View.VISIBLE);
            rv_main_content.setVisibility(View.GONE);
        } else {
            rv_main_content.setVisibility(View.VISIBLE);
            mainContentRVAdapter.readDataFromDB(readListContent());
            //set footer
            View footer = LayoutInflater.from(this).inflate(R.layout.layout_main_list_footer, rv_main_content, false);
            mainContentRVAdapter.setFooterView(footer);
            rv_main_content.setAdapter(mainContentRVAdapter);

            mainContentRVAdapter.setOnItemClickListener(new MainContentRVAdapter.OnRCVItemClickListener() {
                @Override
                public void onItemClick(View view, int position, ListBean listBean) {
                    Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                    intent.putExtra("list_id", String.valueOf(listBean.getId()))
                            .putExtra("list_content", listBean.getContent())
                            .putExtra("list_from", listBean.getFrom())
                            .putExtra("list_date", listBean.getDate());
                    startActivity(intent);
                }

                @Override
                public void onItemLikeClick(ImageView iv_favorite, int position) {
                    iv_favorite.setImageResource(R.drawable.ic_favorite_normal);
                    Toast.makeText(getApplicationContext(), "这是第" + position + "个star", Toast.LENGTH_SHORT).show();
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


    public ListBean insertListContent(String content, String date, String from) {
        ListBeanDao listBeanDao = GreenDaoManager.getInstance().getSession().getListBeanDao();
        ListBean listBean = new ListBean(content, date, from);
        listBeanDao.insert(listBean);
        return listBean;
    }

    public List<ListBean> readListContent() {
        listData = new ArrayList<>();
        listData = DBUtils.readAllList();
        return listData;
    }

    public List<AppBean> readNaviBeanList() {
        List<AppBean> appBeanList;
        appBeanList = DBUtils.readNavigationList();
        return appBeanList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainContentRVAdapter.readDataFromDB(readListContent());
        rv_main_content.setAdapter(mainContentRVAdapter);

//        naviListAdapter.readListFromDB(readNaviBeanList());
//        naviListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);

        final MenuItem item_search = menu.findItem(R.id.action_search);
        final MenuItem item_setting = menu.findItem(R.id.action_setting);

        item_search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return false;
            }
        });

        item_setting.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
