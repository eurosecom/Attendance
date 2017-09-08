package com.eusecom.attendance.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String ustype;
    public String usico;
    public String usosc;
    public String usatw;
    public String keyf;
    public String admin;
    public String lati;
    public String longi;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.keyf = "0";
        this.admin = "0";
        this.lati = "0";
        this.longi = "0";
    }

    public User(String username, String email, String ustype, String usico, String usatw) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = "0";
        this.usatw = usatw;
        this.keyf = "0";
        this.admin = "0";
        this.lati = "0";
        this.longi = "0";
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public User(String username, String email, String ustype, String usico, String usosc, String usatw) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = usosc;
        this.usatw = usatw;
        this.keyf = "0";
        this.admin = "0";
        this.lati = "0";
        this.longi = "0";
    }

    public User(String username, String email, String ustype, String usico, String usosc, String usatw, String keyf) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = usosc;
        this.usatw = usatw;
        this.keyf = keyf;
        this.admin = "0";
        this.lati = "0";
        this.longi = "0";
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getUstype() {

        return this.ustype;

    }

    public String getUsername() {

        return this.username;

    }

    public String getUsico() {

        return this.usico;

    }

    public String getUsosc() {

        return this.usosc;

    }

}
// [END blog_user_class]
