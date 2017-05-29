package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_employee_class]
@IgnoreExtraProperties
public class Employee {

    public String username;
    public String email;
    public String ustype;
    public String usico;
    public String usosc;
    public String usatw;

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
    }

    public Employee(String username, String usosc ) {
        this.username = username;
        this.email = "";
        this.ustype = "";
        this.usico = "";
        this.usosc = usosc;
        this.usatw = "0";
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
    public String getUsername() {

        return username;
    }
}
// [END blog_employee_class]
