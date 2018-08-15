package com.youli.feiyu.fynotification.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youli.feiyu.fynotification.R;
import com.youli.feiyu.fynotification.bean.ItemBean;
import com.youli.feiyu.fynotification.utility.ItemsDBHelper;

import java.util.ArrayList;

/**
 * Created by Sean on 16/2/23.
 */
public class DataShowerAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<ItemBean> mItems;
    private ItemsDBHelper mItemsDBHelper;

    public DataShowerAdapter(Context context) {
        this.mContext = context;
        this.mItemsDBHelper = new ItemsDBHelper(this.mContext);
        this.mItems = mItemsDBHelper.loadItems();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DataShowerHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.data_shower_item, null);

            holder = new DataShowerHolder();
            holder.tvSms = (TextView)convertView.findViewById(R.id.tv_sms);
            holder.tvNumber = (TextView)convertView.findViewById(R.id.tv_number);

            convertView.setTag(holder);//绑定ViewHolder对象

        } else {
            holder = (DataShowerHolder)convertView.getTag();//取出ViewHolder对象
        }

        holder.tvSms.setText(mItems.get(position).getSms());
        holder.tvNumber.setText(mItems.get(position).getNumber());

        return convertView;
    }

    public void deleteItem(ItemBean itemBean) {
        mItems.remove(itemBean);
        mItemsDBHelper.deleteItem(itemBean);
        notifyDataSetChanged();
    }

    private class DataShowerHolder {
        public TextView tvSms;
        public TextView tvNumber;
    }
}
