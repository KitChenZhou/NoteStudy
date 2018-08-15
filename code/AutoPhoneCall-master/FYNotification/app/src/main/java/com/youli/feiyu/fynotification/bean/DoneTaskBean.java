package com.youli.feiyu.fynotification.bean;

/**
 * Created by Sean on 16/2/26.
 */
public class DoneTaskBean {
    private String phoneNumber;
    private long addTime;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "CurrentTaskBean{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", addTime='" + addTime + '\'' +
                '}';
    }

}
