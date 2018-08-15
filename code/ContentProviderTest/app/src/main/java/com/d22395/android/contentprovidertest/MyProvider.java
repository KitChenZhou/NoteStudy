package com.d22395.android.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by D22395 on 2017/9/6.
 */

public class MyProvider extends ContentProvider {

    private static final int TABLE1_DIR = 0;
    private static final int TABLE1_ITEM = 1;
    private static final int TABLE2_DIR = 2;
    private static final int TABLE2_ITEM = 3;

    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI("com.d22395.android.contentprovidertest.provider", "table1", TABLE1_DIR);
        sUriMatcher.addURI("com.d22395.android.contentprovidertest.provider", "table1/#", TABLE1_ITEM);
        sUriMatcher.addURI("com.d22395.android.contentprovidertest.provider", "table2", TABLE2_DIR);
        sUriMatcher.addURI("com.d22395.android.contentprovidertest.provider", "table2/#", TABLE2_ITEM);
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        switch (sUriMatcher.match(uri)) {
            case TABLE1_DIR:
                // 查询table1表中的所有数据
                break;
            case TABLE1_ITEM:
                // 查询table1表中的单条数据
                break;
            case TABLE2_DIR:
                break;
            case TABLE2_ITEM:
                break;
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.d22395.android.contentprovidertest.provider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/.vnd.com.d22395.android.contentprovidertest.provider.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.d22395.android.contentprovidertest.provider.table2";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.d22395.android.contentprovidertest.provider.table2";
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
