package com.eusecom.attendance;

import com.eusecom.attendance.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by chris on 6/1/16.
 */
public interface GitHubService {
    @GET("users/{user}/starred")
    Observable<List<GitHubRepo>> getStarredRepositories(@Path("user") String userName);

    @POST("/fcm/send")
    Call<Message> sendMessage(@Body Message message);



}
