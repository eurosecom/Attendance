package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

// [START List_class]
@IgnoreExtraProperties
public class MyList {

    public String Listname;
    HashMap<String, String> myusers;
    public String idm;
    public String iname;
    private HashMap<String, Object> dats;


    public MyList() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public MyList(String Listname, HashMap<String, String> myusers, String idm, String iname) {
        this.Listname = Listname;
        this.myusers = myusers;
        this.idm = idm;
        this.iname = iname;
        HashMap<String, Object> datsObj = new HashMap<String, Object>();
        datsObj.put("date", ServerValue.TIMESTAMP);
        this.dats = datsObj;

    }


    // [START List_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Listname", Listname);
        result.put("Users", myusers);
        result.put("idm", idm);
        result.put("iname", iname);
        result.put("dats", dats);

        return result;
    }
    // [END List_to_map]


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
// [END List_class]
