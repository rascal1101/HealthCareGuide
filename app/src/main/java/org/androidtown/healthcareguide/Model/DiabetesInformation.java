package org.androidtown.healthcareguide.Model;

/**
 * Created by yjhyj on 2017-11-21.
 */

public class DiabetesInformation {
    private String date;
    private String time;
    private String eat;
    private String diabetesinfo;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DiabetesInformation(){}

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

    public String getEat() {
        return eat;
    }

    public void setEat(String eat) {
        this.eat = eat;
    }

    public String getDiabetesinfo() {
        return diabetesinfo;
    }

    public void setDiabetesinfo(String diabetesinfo) {
        this.diabetesinfo = diabetesinfo;
    }
}
