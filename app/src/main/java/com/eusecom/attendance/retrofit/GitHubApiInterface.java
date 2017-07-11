package com.eusecom.attendance.retrofit;

import com.eusecom.attendance.models.Repository;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubApiInterface {

    @GET("/users/{user}/repos")
    Call<ArrayList<Repository>> getRepository(@Path("user") String userName);


    @GET("/users/{user}/repos")
    Observable<List<Repository>> getRxRepository(@Path("user") String userName);

}