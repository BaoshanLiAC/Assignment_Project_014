package com.ac.assignment_project_014.recipe;

import com.google.gson.annotations.SerializedName;

public class Post {

private int userId;

private int id;

private String title;

//@SerializedName("body")
private String body;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return body;
    }
}
