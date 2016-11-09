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

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, String ustype, String usico) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
    }

    public User(String username, String email, String ustype, String usico, String usosc) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = usosc;
    }

    public String getUstype() {

        return this.ustype;

    }

    public String getUsico() {

        return this.usico;

    }

}
// [END blog_user_class]
