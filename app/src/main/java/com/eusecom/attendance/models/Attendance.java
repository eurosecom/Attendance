package com.eusecom.attendance.models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class Attendance {

    public String usico;
    public String usid;
    public String ume;
    public String dmxa;
    public String daod;
    public String dado;
    public String dnixa;
    public String hodxb;
    public String longi;
    public String lati;

    public Attendance() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }



    public Attendance(String usico, String usid, String ume, String dmxa, String daod, String dado, String dnixa,
                      String hodxb, String longi, String lati) {
        this.usico = usico;
        this.usid = usid;
        this.ume = ume;
        this.dmxa = dmxa;
        this.daod = daod;
        this.dado = dado;
        this.dnixa = dnixa;
        this.hodxb = hodxb;
        this.longi = longi;
        this.lati = lati;
    }


    public String getUsico() {

        return this.usico;

    }

}
// [END blog_user_class]
