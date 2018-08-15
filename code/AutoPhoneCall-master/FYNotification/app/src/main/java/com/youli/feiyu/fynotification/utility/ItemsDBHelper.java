package com.youli.feiyu.fynotification.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.config.Config;

import java.util.ArrayList;

/**
 * Created by Sean on 16/2/25.
 */
public class ItemsDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Items.db";

    public static final String TABLE_NAME = "items";
    public static final String COLUMN_SMS = "sms";
    public static final String COLUMN_NUMBER = "number";

    public static final String CREATE_TABLE ="create table " + TABLE_NAME + " ("
            + "id integer primary key autoincrement,"
            + COLUMN_SMS +" text, "
            + COLUMN_NUMBER + " text)";

    private Context mContext;
    private SQLiteDatabase mDB;

    public ItemsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mDB = this.getWritableDatabase();
    }

    public void saveItem(ItemBean itemBean) {
        if(hasItem(itemBean)) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SMS, itemBean.getSms());
        values.put(COLUMN_NUMBER, itemBean.getNumber());
        mDB.insert(TABLE_NAME, "", values);
    }

    public void deleteItem(ItemBean itemBean) {
        String selection = COLUMN_SMS + "=? AND " + COLUMN_NUMBER + "=?";
        String[] selectionArgs = {itemBean.getSms(), itemBean.getNumber()};
        mDB.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void updateItem(ItemBean oldItemBean, ItemBean newItemBean) {
//        deleteItem(oldItemBean);
//        saveItem(newItemBean);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, newItemBean.getNumber());
        values.put(COLUMN_SMS, newItemBean.getSms());

        String selection = COLUMN_SMS + "=? AND " + COLUMN_NUMBER + "=?";
        String[] selectionArgs = {oldItemBean.getSms(), oldItemBean.getNumber()};

        mDB.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public ArrayList<ItemBean> loadItems() {
        ArrayList<ItemBean> itemBeans = new ArrayList<ItemBean>();
        Cursor cursor = mDB.query(TABLE_NAME, null, null, null, null, null,null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                ItemBean itemBean = new ItemBean();
                itemBean.setSms(cursor.getString(cursor.getColumnIndex(COLUMN_SMS)));
                itemBean.setNumber(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER)));
                itemBeans.add(itemBean);
            }
        }
        Log.i(Config.TAG, "ItemsDBHelper->loadItems: " + itemBeans);
        return itemBeans;
    }

    public ArrayList<String> getNumbers(String sms) {
        ArrayList<String> sNumbers = new ArrayList<String>();

        String selection = COLUMN_SMS + "=?";
        String[] selectionArgs = new String[] {sms};
        Cursor cursor = mDB.query(TABLE_NAME, null, selection, selectionArgs, null, null,null);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                String sNumber = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
                sNumbers.add(sNumber);
            }
        }
        return sNumbers;
    }

    public boolean hasItem(ItemBean itemBean) {
        String selection = COLUMN_SMS + "=? AND " + COLUMN_NUMBER + "=?";
        String[] selectionArgs = new String[] {itemBean.getSms(), itemBean.getNumber()};
        Cursor cursor = mDB.query(TABLE_NAME, null, selection, selectionArgs, null, null,null);
        if(cursor == null || cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
