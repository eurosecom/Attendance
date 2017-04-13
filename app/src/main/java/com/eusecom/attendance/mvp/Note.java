package com.eusecom.attendance.mvp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START Nota_class]
@IgnoreExtraProperties
public class Note {

    public String text;
    public String date;


    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(interrupt.class)
    }

    public Note(String text, String date ) {
        this.text = text;
        this.date = date;

    }



    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public String getText() {
        return text;
    }

}
// [END Nota_class]
