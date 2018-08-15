package com.youli.feiyu.fynotification.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.youli.feiyu.fynotification.bean.DoneTaskBean;
import com.youli.feiyu.fynotification.config.Config;

/**
 * Created by Sean on 16/2/26.
 */
public class DoneTaskDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DoneTask.db";

    public static final String TABLE_NAME = "done_task";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_ADD_TIME = "add_time";

    public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
            + "id integer primary key autoincrement,"
            + COLUMN_PHONE_NUMBER + " text, "
            + COLUMN_ADD_TIME + " integer)";

    private Context context;
    private SQLiteDatabase db;

    public DoneTaskDBHelper(Context context) {
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

    public boolean hasItemWithNumber(String phoneNumber) {
        String selection = COLUMN_PHONE_NUMBER + "=?";
        String[] selectionArgs = new String[]{phoneNumber};
        Cursor cursor = this.db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void updateItem(DoneTaskBean bean) {
//        Log.d(Config.TAG, "DoneTaskDBHelper->updateItem: " + bean);
        if (hasItemWithNumber(bean.getPhoneNumber())) {
            deleteItem(bean);
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE_NUMBER, bean.getPhoneNumber());
        values.put(COLUMN_ADD_TIME, bean.getAddTime());
        db.insert(TABLE_NAME, "", values);
    }

    public void deleteItem(DoneTaskBean bean) {
//        Log.d(Config.TAG, "DoneTaskDBHelper->deleteItem: " + bean);
        String selection = COLUMN_PHONE_NUMBER + "=?";
        String[] selectionArgs = {bean.getPhoneNumber()};
        this.db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public DoneTaskBean getItemWithNumber(String phoneNumber) {
        String selection = COLUMN_PHONE_NUMBER + "=?";
        String[] selectionArgs = new String[]{phoneNumber};
        Cursor cursor = this.db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String sNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
                long sTime = cursor.getLong(cursor.getColumnIndex(COLUMN_ADD_TIME));

                DoneTaskBean bean = new DoneTaskBean();
                bean.setPhoneNumber(sNumber);
                bean.setAddTime(sTime);

//                Log.d(Config.TAG, "DoneTaskDBHelper->getLastItem: " + bean);
                return bean;
            }
        }
        return null;
    }
}
