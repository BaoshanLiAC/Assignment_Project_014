/**
  * Copyright 2020 bejson.com 
  */
package com.ac.assignment_project_014.recipe.JsonEntityClass;
import java.util.List;

/**
 * Auto-generated: 2020-11-03 21:17:14
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String title;
    private double version;
    private String href;
    private List<Results> results;
    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setVersion(double version) {
         this.version = version;
     }
     public double getVersion() {
         return version;
     }

    public void setHref(String href) {
         this.href = href;
     }
     public String getHref() {
         return href;
     }

    public void setResults(List<Results> results) {
         this.results = results;
     }
     public List<Results> getResults() {
         return results;
     }

}