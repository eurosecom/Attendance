package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START Poll_class]
@IgnoreExtraProperties
public class Poll {

    public String idm;
    public String iname;


    public Poll() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Poll(String idm, String iname) {
        this.idm = idm;
        this.iname = iname;

    }

    // [START Poll_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idm", idm);
        result.put("iname", iname);

        return result;
    }
    // [END Poll_to_map]

    public String getQuestion() {

        return this.iname;

    }

}
// [END Poll_class]
