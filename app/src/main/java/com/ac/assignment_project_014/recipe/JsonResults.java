/**
  * Copyright 2020 bejson.com 
  */
package com.ac.assignment_project_014.recipe;

/**
 * Auto-generated: 2020-11-03 21:17:14
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonResults {

    private String title;
    private String href;
    private String ingredients;

    public JsonResults(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

    private String thumbnail;
    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setHref(String href) {
         this.href = href;
     }
     public String getHref() {
         return href;
     }

    public void setIngredients(String ingredients) {
         this.ingredients = ingredients;
     }
     public String getIngredients() {
         return ingredients;
     }

    public void setThumbnail(String thumbnail) {
         this.thumbnail = thumbnail;
     }
     public String getThumbnail() {
         return thumbnail;
     }



}