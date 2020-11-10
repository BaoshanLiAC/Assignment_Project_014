package com.ac.assignment_project_014.recipe;

import com.ac.assignment_project_014.recipe.JsonEntityClass.JsonRootBean;

import retrofit2.Call;
import retrofit2.http.GET;


public interface JsonPlaceHolderAPI {

    @GET ("api")
    Call<JsonRootBean> getPosts();
}
