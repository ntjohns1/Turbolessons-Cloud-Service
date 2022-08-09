package com.noslen.eventservice.dto;


public class LessonEvent {

    private static int id = 1;
    private String date;

    public LessonEvent(String date) {

        this.date = date;
        id++;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        LessonEvent.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
