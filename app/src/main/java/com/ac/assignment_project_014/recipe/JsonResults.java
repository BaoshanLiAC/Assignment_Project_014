/**
  * Copyright 2020 bejson.com 
  */
package com.ac.assignment_project_014.recipe;

/**
 * The entity class of Recipe
 * Auto-generated: 2020-11-03 21:17:14
 *
 * @author chunyan ren
 * reference http://www.bejson.com/java2pojo/
 */
public class JsonResults {
    /**
     * title of the Recipe
     */
    private String title;
    /**
     * href of the Recipe
     */
    private String href;
    /**
     * ingredients of the Recipe
     */
    private String ingredients;
    /**
     * image of the Recipe
     */
    private String thumbnail;
    /**
     * image of the Recipe
     */
    public JsonResults(String title, String href, String ingredients, String thumbnail) {
        this.title = title;
        this.href = href;
        this.ingredients = ingredients;
        this.thumbnail = thumbnail;
    }

    /**
     * set the title of the Recipe
     * @param title the title of recipe
     */
    public void setTitle(String title) {
         this.title = title;
     }
    /**
     * get the title of the Recipe
     * @return the title the url of recipe
     */
     public String getTitle() {
         return title;
     }
    /**
     * set the image of the Recipe
     * @param href the url of recipe
     */
    public void setHref(String href) {
         this.href = href;
     }
    /**
     * get the url bind to the Recipe
     * @return  the url of recipe
     */
     public String getHref() {
         return href;
     }
    /**
     * set the ingredients of the Recipe
     * @param ingredients the ingredients included in the recipe
     */
    public void setIngredients(String ingredients) {
         this.ingredients = ingredients;
     }
    /**
     * image of the Recipe
     * @return the ingredients included in the recipe
     */
     public String getIngredients() {
         return ingredients;
     }
    /**
     * set image of the Recipe
     * @param thumbnail the image included in the recipe
     */
    public void setThumbnail(String thumbnail) {
         this.thumbnail = thumbnail;
     }
    /**
     * get image of the Recipe
     * @return the image included in the recipe
     */
     public String getThumbnail() {
         return thumbnail;
     }



}