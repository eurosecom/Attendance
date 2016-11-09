package com.eusecom.attendance.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_employee_class]
@IgnoreExtraProperties
public class Company {

    public String cmico;
    public String cmname;
    public String cmdom;
    public String cmfir;

    public Company() {
        // Default constructor required for calls to DataSnapshot.getValue(Company.class)
    }

    public Company(String cmico, String cmname, String cmdom, String cmfir) {
        this.cmico = cmico;
        this.cmname = cmname;
        this.cmdom = cmdom;
        this.cmfir = cmfir;
    }

}
// [END blog_employee_class]
