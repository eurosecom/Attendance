package com.eusecom.attendance;

import android.support.annotation.NonNull;

import com.eusecom.attendance.models.Message;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by chris on 6/1/16.
 */
public class FbmessClient {

    private static final String FCM_BASE_URL = "https://fcm.googleapis.com";
    private static final String GITHUB_BASE_URL = "https://api.github.com/";

    private static FbmessClient instance;
    private FbmessService fbmessService;

    private FbmessClient() {
        final Gson gson =
            new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String keyid="key=" + Constants.LEGACY_SERVER_KEY;
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", keyid); // <-- this is the important line
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        httpClient.addInterceptor(logging);
        OkHttpClient client = httpClient.build();


        final Retrofit retrofit = new Retrofit.Builder().baseUrl(FCM_BASE_URL)
                                                        .client(client)
                                                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                                        .build();
        fbmessService = retrofit.create(FbmessService.class);
    }

    public static FbmessClient getInstance() {
        if (instance == null) {
            instance = new FbmessClient();
        }
        return instance;
    }

    public Observable<Message> sendmyMessage(@NonNull String userName,  Message message) {
        //aa return fbmessService.rfsendmyMessage(userName);
        return fbmessService.rxsendMessage(message);
    }
}
