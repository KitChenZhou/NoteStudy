package com.ckt.demo.mvptest.biz;

import com.ckt.demo.mvptest.bean.User;

/**
 * Created by D22395 on 2018/7/19.
 */

public interface OnLoginListener
{
    void loginSuccess(User user);

    void loginFailed();
}
