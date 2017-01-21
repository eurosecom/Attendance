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
    private HashMap<String, Object> dats;


    public Abstype() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Abstype(String idm, String iname) {
        this.idm = idm;
        this.iname = iname;

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

}
// [END AbsType_class]
