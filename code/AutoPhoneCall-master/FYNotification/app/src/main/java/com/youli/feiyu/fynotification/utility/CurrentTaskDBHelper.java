package com.youli.feiyu.fynotification.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.youli.feiyu.fynotification.bean.CurrentTaskBean;
import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.config.Config;

/**
 * Created by Sean on 16/2/26.
 */
public class CurrentTaskDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CurrentTask.db";

    public static final String TABLE_NAME = "current_task";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_ADD_TIME = "add_time";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + "id integer primary key autoincrement,"
            + COLUMN_PHONE_NUMBER + " text, "
            + COLUMN_ADD_TIME + " integer)";

    private Context context;
    private SQLiteDatabase db;

    public CurrentTaskDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean hasItem(CurrentTaskBean bean) {
        String selection = COLUMN_PHONE_NUMBER + "=?";
        String[] selectionArgs = new String[]{bean.getPhoneNumber()};
        Cursor cursor = this.db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void saveItem(CurrentTaskBean bean) {
        if (hasItem(bean)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE_NUMBER, bean.getPhoneNumber());
        values.put(COLUMN_ADD_TIME, bean.getAddTime());
        db.insert(TABLE_NAME, "", values);
    }

    public void deleteItemWithNumber(String number) {
        String selection = COLUMN_PHONE_NUMBER + "=?";
        String[] selectionArgs = {number};
        this.db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public CurrentTaskBean getLastItem() {
        String orderBy = COLUMN_ADD_TIME  + " ASC";
        String limit = "1";
        Cursor cursor = this.db.query(false, TABLE_NAME, null, null, null, null, null, orderBy, limit);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String sNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
                long sTime = cursor.getLong(cursor.getColumnIndex(COLUMN_ADD_TIME));

                CurrentTaskBean bean = new CurrentTaskBean();
                bean.setPhoneNumber(sNumber);
                bean.setAddTime(sTime);

//                Log.d(Config.TAG, "CurrentTaskDBHelper->getLastItem: " + bean);
                return bean;
            }
        }
        return null;
    }
}
