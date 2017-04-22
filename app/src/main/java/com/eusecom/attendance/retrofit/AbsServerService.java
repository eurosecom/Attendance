package com.eusecom.attendance.retrofit;

import com.eusecom.attendance.models.Attendance;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface AbsServerService {

    @GET("/attendance/absserver.php")
    Observable<List<Attendance>> getAbsServer(@Query("user") String userName);

    @GET("/attendance/absserver.json")
    Observable<List<Attendance>> getAbsServerFromJson(@Query("user") String userName);

    @GET("users/{user}/starred")
    Observable<List<Attendance>> getAbsServerFromGitHub(@Path("user") String userName);


}
