package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_employee_class]
@IgnoreExtraProperties
public class Employee {

    public String username;
    public String email;
    public String ustype;
    public String usico;
    public String usosc;
    public String usatw;
    public String keyf;
    public String admin;

    public Employee() {
        // Default constructor required for calls to DataSnapshot.getValue(Employee.class)
    }

    public Employee(String username, String email, String ustype, String usico, String usatw) {
        this.username = username;
        this.email = email;
        this.ustype = ustype;
        this.usico = usico;
        this.usosc = "0";
        this.usatw = usatw;
        this.keyf = "0";
        this.admin = "0";
    }

    public Employee(String username, String usosc ) {
        this.username = username;
        this.email = "";
        this.ustype = "";
        this.usico = "";
        this.usosc = usosc;
        this.usatw = "0";
        this.keyf = "0";
        this.admin = "0";
    }

    @Exclude
    public String getEmail() {

        return email;
    }

    @Exclude
    public  void setEmail(String email) {

        this.email = email;
    }

    @Exclude
    public  void setUsatw(String usatw) {

        this.usatw = usatw;
    }

    @Exclude
    public String getUsatw() {

        return usatw;
    }

    @Exclude
    public  void setUsername(String username) {

        this.username = username;
    }

    @Exclude
    public String getUsername() {

        return username;
    }

    @Exclude
    public  void setUsosc(String usosc) {

        this.usosc = usosc;
    }

    @Exclude
    public String getUsosc() {

        return usosc;
    }

    @Exclude
    public  void setUsico(String usico) {

        this.usico = usico;
    }

    @Exclude
    public String getUsico() {

        return usico;
    }

    @Exclude
    public  void setUstype(String ustype) {

        this.ustype = ustype;
    }

    @Exclude
    public String getUstype() {

        return ustype;
    }

    @Exclude
    public  void setKeyf(String keyf) {

        this.keyf = keyf;
    }

    @Exclude
    public String getKeyf() {

        return keyf;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);
        result.put("usosc", usosc);
        result.put("usico", usico);
        result.put("ustype", ustype);
        result.put("usatw", usatw);
        result.put("keyf", keyf);
        result.put("admin", admin);

        return result;
    }

}
// [END blog_employee_class]
