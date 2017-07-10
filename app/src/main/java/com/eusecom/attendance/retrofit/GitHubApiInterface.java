package com.eusecom.attendance.retrofit;

import com.eusecom.attendance.models.Repository;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//APIinterface for demo dagger2 retrofit

public interface GitHubApiInterface {
    @GET("/users/{user}/repos")
    Call<ArrayList<Repository>> getRepository(@Path("user") String userName);

}