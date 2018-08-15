package com.ckt.demo.mvptest.biz;

/**
 * Created by D22395 on 2018/7/19.
 */

public interface IUserBiz
{
    public void login(String username, String password, OnLoginListener loginListener);
}
