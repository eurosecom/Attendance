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

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, String ustype, String usico, String usatw) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = "0";
        this.usatw = usatw;
    }

    public User(String username, String email, String ustype, String usico, String usosc, String usatw) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = usosc;
        this.usatw = usatw;
    }

    public String getUstype() {

        return this.ustype;

    }

    public String getUsname() {

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
