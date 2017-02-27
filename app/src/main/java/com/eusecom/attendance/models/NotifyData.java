package com.eusecom.attendance.models;


public class NotifyData {
    String title;
    String body;
    String sound;

    public NotifyData(String title, String body ) {

        this.title = title;
        this.body = body;
        this.sound = "default";
    }

    }