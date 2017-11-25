package org.androidtown.healthcareguide.Model;

/**
 * Created by yjhyj on 2017-11-24.
 */

public class BloodPressureInformation {
    private String date;
    private String time;
    private String bloodHigh;
    private String bloodLow;
    private String key;

    public BloodPressureInformation(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBloodHigh() {
        return bloodHigh;
    }

    public void setBloodHigh(String bloodHigh) {
        this.bloodHigh = bloodHigh;
    }

    public String getBloodLow() {
        return bloodLow;
    }

    public void setBloodLow(String bloodLow) {
        this.bloodLow = bloodLow;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
