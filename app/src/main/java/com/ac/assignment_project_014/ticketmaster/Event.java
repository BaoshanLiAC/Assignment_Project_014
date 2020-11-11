package com.ac.assignment_project_014.ticketmaster;

import java.util.Date;

public class Event {
    private long id;
    private String name;
    private boolean isSent;
    private boolean isReceived;

    public Event(long id, String name, boolean isSent, boolean isReceived){
        this.id = id;
        this.name = name;
        this.isSent = isSent;
        this.isReceived = isReceived;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String message) {
        this.name = name;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }
}
