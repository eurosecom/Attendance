package com.eusecom.attendance.retrofit;

import com.eusecom.attendance.models.Attendance;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/*
<?php

//read headers
        $headerStringValue="";
        $headers = apache_request_headers();

        foreach ($headers as $header => $value) {
        $headerStringValue = " ".$headerStringValue.$value." ";
        }

        $user = $_GET['user'];

        $sqlt = <<<ico
        {
        "login": "JakeWharton",
        "id": 66577,
        "avatar_url": "https://avatars3.githubusercontent.com/u/66577?v=3",
        "gravatar_id": "",
        "url": "https://api.github.com/users/JakeWharton",
        "html_url": "https://github.com/JakeWharton",
        "followers_url": "https://api.github.com/users/JakeWharton/followers",
        "following_url": "https://api.github.com/users/JakeWharton/following{/other_user}",
        "gists_url": "https://api.github.com/users/JakeWharton/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/JakeWharton/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/JakeWharton/subscriptions",
        "organizations_url": "https://api.github.com/users/JakeWharton/orgs",
        "repos_url": "https://api.github.com/users/JakeWharton/repos",
        "events_url": "https://api.github.com/users/JakeWharton/events{/privacy}",
        "received_events_url": "https://api.github.com/users/JakeWharton/received_events",
        "type": "User",
        "site_admin": false,
        "name": "Jake Wharton",
        "company": "Square, Inc.",
        "blog": "http://jakewharton.com",
        "location": "Pittsburgh, PA, USA",
        "email": "jakewharton@gmail.com
        ico;

        $prem=" from php prem";

        $sqlt1 = <<<ico1
        ",
        "hireable": null,
        "bio": null,
        "public_repos": 92,
        "public_gists": 54,
        "followers": 33187,
        "following": 12,
        "created_at": "2009-03-24T16:09:53Z",
        "updated_at": "2017-03-26T15:19:18Z"
        }
        ico1;

        echo $sqlt.$prem.$headerStringValue.$user.$sqlt1;
 ?>
 */

public interface RfEtestApi {

    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     */
    @FormUrlEncoded
    @POST("/attendance/save_absence.php")
    Observable<List<RfContributor>> contributors(@Field("savetofir") String savetofir, @Field("keyf") String keyf,
                                                 @Field("whoapprove") String whoapprove, @Field("approveabs_json") String approveabs_json,
                                                 @Field("anodaj") int anodaj);

    @GET("/attendance/contributor.php")
    Observable<List<RfContributor>> contributorsGET(@Query("owner") String owner,
                                                 @Query("repo") String repo);

    @GET("/attendance/contributor.json")
    Observable<List<RfContributor>> contributors_json(@Query("owner") String owner,
                                               @Query("repo") String repo);

    @GET("/attendance/{owner}/{repo}/contributor.json")
    Observable<List<RfContributor>> contributors_withPath(@Path("owner") String owner,
                                                 @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<RfContributor>> contributors_orig(@Path("owner") String owner,
                                                 @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    List<RfContributor> getContributors(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * See https://developer.github.com/v3/users/
     */
    @FormUrlEncoded
    @POST("/attendance/email_absence.php")
    Observable<RfUser> user(@Field("savetofir") String savetofir, @Field("keyf") String keyf,
                            @Field("whoapprove") String whoapprove, @Field("approveabs_json") String approveabs_json,
                            @Field("anodaj") int anodaj, @Field("user") String user);

    @GET("/attendance/user.php")
    Observable<RfUser> userold(@Query("user") String user);

    @GET("/users/{user}")
    Observable<RfUser> user_orig(@Path("user") String user);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    RfUser getUser(@Path("user") String user);
}