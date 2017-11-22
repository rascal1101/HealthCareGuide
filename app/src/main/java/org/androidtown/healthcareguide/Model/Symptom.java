package org.androidtown.healthcareguide.Model;

import com.google.firebase.database.ServerValue;

/**
 * Created by MSI on 2017-11-23.
 */

public class Symptom {
    private String uid;
    private String content;
    private String imageUrl;
    private Object timestamp;

    public Symptom(){
        timestamp = ServerValue.TIMESTAMP;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
