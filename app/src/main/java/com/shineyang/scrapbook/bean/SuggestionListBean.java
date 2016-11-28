package com.shineyang.scrapbook.bean;

import android.os.Parcel;

/**
 * Created by ShineYang on 2016/11/28.
 */

public class SuggestionListBean implements com.arlib.floatingsearchview.suggestions.model.SearchSuggestion {

    private String searchResName;

    public SuggestionListBean(Parcel parcel) {
        this.searchResName = parcel.readString();
    }

    public SuggestionListBean(String suggestions) {
        this.searchResName = suggestions;
    }

    @Override
    public String getBody() {
        return searchResName;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Creator<SuggestionListBean> CREATOR = new Creator<SuggestionListBean>() {
        @Override
        public SuggestionListBean createFromParcel(Parcel in) {
            return new SuggestionListBean(in);
        }

        @Override
        public SuggestionListBean[] newArray(int size) {
            return new SuggestionListBean[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchResName);
    }

}
