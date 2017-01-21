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
    private HashMap<String, Object> datm;


    public Abstype() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Abstype(String idm, String iname) {
        this.idm = idm;
        this.iname = iname;

        HashMap<String, Object> datmObj = new HashMap<String, Object>();
        datmObj.put("date", ServerValue.TIMESTAMP);
        this.datm = datmObj;

    }

    // [START AbsType_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idm", idm);
        result.put("iname", iname);
        result.put("datm", datm);

        return result;
    }
    // [END AbsType_to_map]


    public HashMap<String, Object> getDatm() {
        //If there is a dateCreated object already, then return that
        if (datm!= null) {
            return datm;
        }
        //Otherwise make a new object set to ServerValue.TIMESTAMP
        HashMap<String, Object> datmObj = new HashMap<String, Object>();
        datmObj.put("date", ServerValue.TIMESTAMP);
        return datmObj;
    }


    @Exclude
    public long getDatmLong() {
        return (long)datm.get("date");
    }

    @Exclude
    public String getDatmString() {

        long datml = (long)datm.get("date");
        String datms = datml + "";

        return datms;
    }

}
// [END AbsType_class]
