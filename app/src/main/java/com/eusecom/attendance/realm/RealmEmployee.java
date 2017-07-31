package com.eusecom.attendance.realm;

import io.realm.RealmObject;

public class RealmEmployee extends RealmObject {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUstype() {
        return ustype;
    }

    public void setUstype(String ustype) {
        this.ustype = ustype;
    }

    public String getUsico() {
        return usico;
    }

    public void setUsico(String usico) {
        this.usico = usico;
    }

    public String getUsosc() {
        return usosc;
    }

    public void setUsosc(String usosc) {
        this.usosc = usosc;
    }

    public String getUsatw() {
        return usatw;
    }

    public void setUsatw(String usatw) {
        this.usatw = usatw;
    }

    public String getKeyf() {
        return keyf;
    }

    public void setKeyf(String keyf) {
        this.keyf = keyf;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    private String username;
    private String email;
    private String ustype;
    private String usico;
    private String usosc;
    private String usatw;
    private String keyf;
    private String admin;

}

