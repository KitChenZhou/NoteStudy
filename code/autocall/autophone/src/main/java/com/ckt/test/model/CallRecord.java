package com.ckt.test.model;

public class CallRecord {

    private int id;
    private String startDate;
    private String endDate;
    private int duration;
    private String networkType;
    private String isConnect;

    public CallRecord(int id, String startDate, String endDate, int duration, String networkType, String isConnect) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.networkType = networkType;
        this.isConnect = isConnect;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNetworkType() {
        return networkType;
    }

    void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String isConnect() {
        return isConnect;
    }

    void setConnect(String connect) {
        isConnect = connect;
    }

    @Override
    public String toString() {
        return "CallRecord{" +
                "id=" + id +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", duration=" + duration +
                ", networkType='" + networkType + '\'' +
                ", isConnect=" + isConnect +
                '}';
    }
}
