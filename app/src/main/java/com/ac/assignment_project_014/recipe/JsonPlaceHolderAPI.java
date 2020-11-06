package com.ac.assignment_project_014.recipe;

import com.ac.assignment_project_014.recipe.JsonEntityClass.JsonRootBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface JsonPlaceHolderAPI {
    @GET ("api")
    Call<JsonRootBean> getPosts();

    @GET ("api/?")
    Call<JsonRootBean> getPosts(@Query("i") String ingredients1);

    @GET ("api/?")
    Call<JsonRootBean> getPosts(@Query("i") String ingredients1,
                                @Query("i") String ingredients2); //,@Query("p") String p
}
