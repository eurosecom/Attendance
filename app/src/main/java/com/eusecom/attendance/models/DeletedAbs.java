package com.eusecom.attendance.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class DeletedAbs {

    public String usico;
    public String usid;
    public String keyx;
    private HashMap<String, Object> dats;
    public String aprv;

    public DeletedAbs() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }



    public DeletedAbs(String usico, String usid, String keyx ) {
        this.usico = usico;
        this.usid = usid;
        this.keyx = keyx;

        HashMap<String, Object> datsObj = new HashMap<String, Object>();
        datsObj.put("date", ServerValue.TIMESTAMP);
        this.dats = datsObj;
        this.aprv = "0";
    }

    public DeletedAbs(String usico, String usid, String keyx, String aprv) {
        this.usico = usico;
        this.usid = usid;
        this.keyx = keyx;

        HashMap<String, Object> datsObj = new HashMap<String, Object>();
        datsObj.put("date", ServerValue.TIMESTAMP);
        this.dats = datsObj;
        this.aprv = aprv;
    }

    public String getUsico() {

        return this.usico;

    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("usico", usico);
        result.put("usid", usid);
        result.put("keyx", keyx);
        result.put("dats", dats);
        result.put("aprv", aprv);

        return result;
    }
    // [END post_to_map]

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
// [END blog_user_class]
