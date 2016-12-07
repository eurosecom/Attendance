package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START absence_class]
@IgnoreExtraProperties
public class Absence {

    public String idm;
    public String iname;


    public Absence() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Absence(String idm, String iname) {
        this.idm = idm;
        this.iname = iname;

    }

    // [START absence_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idm", idm);
        result.put("iname", iname);

        return result;
    }
    // [END absence_to_map]

}
// [END absence_class]
