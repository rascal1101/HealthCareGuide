package org.androidtown.healthcareguide.Model;

/**
 * Created by yjhyj on 2017-11-21.
 */

public class DiabetesInformation {
    private String datetime;
    private String eat;
    private String diabetesinfo;



    public DiabetesInformation(){}


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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
