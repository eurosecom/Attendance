package com.eusecom.attendance.models;

public class EventRxBus {  
    private EventRxBus(){}

    public static class Message {
        public final String message;

        public Message(String message) {
            this.message = message;
        }
    }
}