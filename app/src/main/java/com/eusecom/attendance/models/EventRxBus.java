package com.eusecom.attendance.models;

public class EventRxBus {  
    private EventRxBus(){}

    public static class Message {
        public final String message;

        public Message(String message) {
            this.message = message;
        }
    }

    public static class Absence {
        public String dmxa;
        public String dmna;
        public String daod;
        public String dado;
        public String dnixa;
        public String hodxb;

        public Absence(String dmxa,String dmna,String daod,String dado,String dnixa,String hodxb ) {
            this.dmxa = dmxa;
            this.dmna = dmna;
            this.daod = daod;
            this.dado = dado;
            this.dnixa = dnixa;
            this.hodxb = hodxb;
        }
    }
}