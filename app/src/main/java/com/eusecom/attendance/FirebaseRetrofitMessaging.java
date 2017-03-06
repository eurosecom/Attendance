package com.eusecom.attendance;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.eusecom.attendance.models.MessData;
import com.eusecom.attendance.models.Message;
import com.eusecom.attendance.models.NotifyData;

public class FirebaseRetrofitMessaging {

    String to, title, body;


    public FirebaseRetrofitMessaging(String to, String title, String body) {
        this.to= to;
        this.title = title;
        this.body = body;
    }


    public boolean SendNotification() {

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fcm.googleapis.com")//url of FCM message server
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                .build();

        // prepare call in Retrofit 2.0
        FirebaseAPI firebaseAPI = retrofit.create(FirebaseAPI.class);

        //for messaging server
        MessData messdata = new MessData("This is a GCM Topic Message!");
        NotifyData notifydata = new NotifyData(title,body);

        Call<Message> call2 = firebaseAPI.sendMessage(new Message(to, notifydata, ""));
        call2.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                Log.d("Response ", "onResponse");
                //t1.setText("Notification sent");
                Message message = response.body();
                Log.d("message", message.getMessage_id());

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("Response ", "onFailure");
                //t1.setText("Notification failure");
            }
        });


        return true;
    }


}