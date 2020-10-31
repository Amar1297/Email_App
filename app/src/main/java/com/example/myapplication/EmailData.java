package com.example.myapplication;

import java.util.ArrayList;

public class EmailData {
        private int id;
        private String from;
        private String subject;
        private Boolean attach;
        private String message;
        private Boolean isSelected=false;

    public EmailData(int id, String from, String subject, Boolean attach, String message) {
        this.id = id;
        this.from = from;
        this.subject = subject;
        this.attach = attach;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Boolean getAttach() {
        return attach;
    }

    public void setAttach(Boolean attach) {
        this.attach = attach;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsSelected(boolean selected){
        isSelected=selected;
    }

    public boolean isSelected(){
        return isSelected;
    }
}