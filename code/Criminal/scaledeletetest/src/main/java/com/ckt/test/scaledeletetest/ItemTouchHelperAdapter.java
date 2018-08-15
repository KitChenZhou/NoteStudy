package com.ckt.test.scaledeletetest;

/**
 * Created by D22395 on 2017/10/17.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition,int toPosition);
    //数据删除
    void onItemDissmiss(int position);
}
