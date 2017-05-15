package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

// [START AbsType_class]
@IgnoreExtraProperties
public class Abstype {

    public String idm;
    public String iname;
    public String rok;
    private HashMap<String, Object> dats;


    public Abstype() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Abstype(String idm, String iname, String rok) {
        this.idm = idm;
        this.iname = iname;
        this.rok = rok;

        HashMap<String, Object> datsObj = new HashMap<String, Object>();
        datsObj.put("date", ServerValue.TIMESTAMP);
        this.dats = datsObj;

    }

    // [START AbsType_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idm", idm);
        result.put("iname", iname);
        result.put("rok", rok);
        result.put("dats", dats);

        return result;
    }
    // [END AbsType_to_map]


    public HashMap<String, Object> getDats() {
        //If there is a dateCreated object already, then return that
        if (dats!= null) {
            return dats;
        }
        //Otherwise make a new object set to ServerValue.TIMESTAMP
        HashMap<String, Object> datsObj = new HashMap<String, Object>();
        datsObj.put("date", ServerValue.TIMESTAMP);
        return datsObj;
    }


    @Exclude
    public long getDatsLong() {
        return (long)dats.get("date");
    }

    @Exclude
    public String getDatsString() {

        long datsl = (long)dats.get("date");
        String datss = datsl + "";

        return datss;
    }

    @Exclude
    public  void setRok(String rok) {

        this.rok = rok;
    }

    @Exclude
    public String getRok() {

        return rok;
    }

}
// [END AbsType_class]
