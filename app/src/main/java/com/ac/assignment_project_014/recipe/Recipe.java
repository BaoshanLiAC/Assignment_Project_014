package com.ac.assignment_project_014.recipe;

public class Recipe {

    private long id;
    private String name;
    private boolean isSent;
    private boolean isRecieved;

    public Recipe(long id, String name, boolean isSent, boolean isReceived)
    {
        this.id = id;
        this.name = name;
        this.isSent = isSent;
        this.isRecieved = isReceived;
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

    public boolean isRecieved() {
        return isRecieved;
    }

    public void setRecieved(boolean recieved) {
        isRecieved = recieved;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}


