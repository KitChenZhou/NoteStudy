package com.ckt.demo.mvptest.view;

import com.ckt.demo.mvptest.bean.User;

/**
 * Created by D22395 on 2018/7/19.
 */

public interface IUserLoginView {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

    void showLoading();

    void hideLoading();

    void toMainActivity(User user);

    void showFailedError();

}