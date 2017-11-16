package org.androidtown.healthcareguide.Model;

/**
 * Created by MSI on 2017-11-09.
 */

public class User {
    private String name;
    private String email;
    private String phone;
    private String uid;
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public User(){

    }

    public void setUser(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
