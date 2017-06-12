package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_employee_class]
@IgnoreExtraProperties
public class Company {

    public String cmico;
    public String cmname;
    public String cmcity;
    public String cmdom;
    public String cmfir;

    public Company() {
        // Default constructor required for calls to DataSnapshot.getValue(Company.class)
    }

    public Company(String cmico, String cmname, String cmdom, String cmfir, String cmcity) {
        this.cmico = cmico;
        this.cmname = cmname;
        this.cmcity = cmcity;
        this.cmdom = cmdom;
        this.cmfir = cmfir;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("cmico", cmico);
        result.put("cmname", cmname);
        result.put("cmcity", cmcity);
        result.put("cmdom", cmdom);
        result.put("cmfir", cmfir);

        return result;
    }

    public String getCmico() {
        return cmico;
    }

    public void setCmico(String cmico) {
        this.cmico = cmico;
    }

    public String getCmname() {
        return cmname;
    }

    public void setCmname(String cmname) {
        this.cmname = cmname;
    }

    public String getCmcity() {
        return cmcity;
    }

    public void setCmcity(String cmcity) {
        this.cmcity = cmcity;
    }

    public String getCmdom() {
        return cmdom;
    }

    public void setCmdom(String cmdom) {
        this.cmdom = cmdom;
    }

    public String getCmfir() {
        return cmfir;
    }

    public void setCmfir(String cmfir) {
        this.cmfir = cmfir;
    }



}
// [END blog_employee_class]
