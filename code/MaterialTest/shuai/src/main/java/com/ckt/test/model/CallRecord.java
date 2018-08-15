package com.ckt.test.model;

public class CallRecord {
    private int id;
    private String startTime;
    private String endTime;
    private int duration;
    private String networkType;
    private String isSuccessful;

    public CallRecord(int id, String startTime, String endTime,
                      int duration, String networkType, String isSuccessful) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.networkType = networkType;
        this.isSuccessful = isSuccessful;
    }

    public int getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String toString() {
        return "CallRecord{" +
                "id=" + id +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", duration=" + duration +
                ", networkType='" + networkType + '\'' +
                ", isSuccessful=" + isSuccessful +
                '}' + "\n";
    }
}
