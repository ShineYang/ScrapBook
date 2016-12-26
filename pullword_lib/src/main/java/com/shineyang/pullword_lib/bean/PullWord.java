package com.shineyang.pullword_lib.bean;

import android.content.Context;
import android.support.annotation.StringDef;

import com.shineyang.pullword_lib.action.Action;
import com.shineyang.pullword_lib.parser.NetworkParser;
import com.shineyang.pullword_lib.parser.SimpleParser;

import java.lang.annotation.Retention;
import java.util.HashMap;
import java.util.Map;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by ShineYang on 2016/12/24.
 */

public class PullWord {
    public static final String ACTION_SEARCH = "search";
    public static final String ACTION_SHARE = "share";
    public static final String ACTION_COPY = "copy";
    public static final String ACTION_BACK = "back";
    private static SimpleParser sParser;
    private static int sItemSpace;
    private static int sLineSpace;
    private static int sItemTextSize;

    public static void setStyle(int itemSpace, int lineSpace, int itemTextSize) {
        sItemSpace = itemSpace;
        sLineSpace = lineSpace;
        sItemTextSize = itemTextSize;
    }

    @StringDef({ACTION_SEARCH, ACTION_SHARE, ACTION_COPY, ACTION_BACK})
    @Retention(SOURCE)
    public @interface ActionType {

    }

    private static Map<String, Action> mActionMap = new HashMap<>();

    private PullWord() {
    }

    public static void registerAction(@ActionType String type, Action action) {
        mActionMap.put(type, action);
    }

    public static void unregisterAction(@ActionType String type) {
        mActionMap.remove(type);
    }

    public static Action getAction(@ActionType String type) {
        return mActionMap.get(type);
    }

    public static void startAction(Context context, @ActionType String type, String text) {
        Action action = PullWord.getAction(type);
        if (action != null) {
            action.start(context, text);
        }
    }

    public static SimpleParser getSegmentParser() {
        if (sParser == null) {
            // TODO Default parser
            sParser = new NetworkParser();
        }
        return sParser;
    }

    public static void setSegmentParser(SimpleParser parser) {
        sParser = parser;
    }

    public static int getItemSpace() {
        return sItemSpace;
    }

    public static int getLineSpace() {
        return sLineSpace;
    }

    public static int getItemTextSize() {
        return sItemTextSize;
    }
}
