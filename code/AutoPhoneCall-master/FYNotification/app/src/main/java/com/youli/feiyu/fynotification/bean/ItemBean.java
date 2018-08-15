package com.youli.feiyu.fynotification.bean;

/**
 * Created by Sean on 16/2/25.
 */
public class ItemBean {
    private String sms;
    private String number;

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "sms='" + sms + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
