package com.eusecom.attendance.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_employee_class]
@IgnoreExtraProperties
public class Employee {

    public String emico;
    public String emname;
    public String emsurn;
    public String emosc;
    public String emema;

    public Employee() {
        // Default constructor required for calls to DataSnapshot.getValue(Employee.class)
    }

    public Employee(String emico, String emname, String emsurn, String emosc, String emema) {
        this.emico = emico;
        this.emname = emname;
        this.emsurn = emsurn;
        this.emosc = emosc;
        this.emema = emema;
    }

}
// [END blog_employee_class]
