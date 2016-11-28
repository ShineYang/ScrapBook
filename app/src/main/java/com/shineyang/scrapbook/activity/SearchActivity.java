package com.shineyang.scrapbook.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.shineyang.scrapbook.R;
import com.shineyang.scrapbook.adapter.SearchResultListAdapter;
import com.shineyang.scrapbook.bean.ListBean;
import com.shineyang.scrapbook.bean.SuggestionListBean;
import com.shineyang.scrapbook.utils.DBUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;
    @BindView(R.id.rl_search_tips)
    RelativeLayout rl_search_tips;

    private SearchResultListAdapter searchResultListAdapter;
    private static List<SuggestionListBean> querySuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initSearchView();

    }

    public void initSearchView() {
        floatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                finish();
            }
        });

        floatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                rl_search_tips.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFocusCleared() {
                rl_search_tips.setVisibility(View.VISIBLE);

            }
        });


        //监听文字改变
        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    floatingSearchView.clearSuggestions();
                } else {
                    //floatingSearchView.showProgress();
                    List<ListBean> listRes;
                    listRes = DBUtils.queryItem(newQuery);
                    querySuggestions = new ArrayList<>();
                    for (int i = 0; i < listRes.size(); i++) {
                        SuggestionListBean suggestionListBean = new SuggestionListBean(listRes.get(i).getContent());
                        querySuggestions.add(suggestionListBean);
                    }
                    floatingSearchView.swapSuggestions(querySuggestions);
                    //floatingSearchView.hideProgress();
                }
            }
        });

        floatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, final SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, EditorActivity.class);
                        intent.putExtra("list_content", item.getBody());
                        startActivity(intent);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TO DO
    }

}
