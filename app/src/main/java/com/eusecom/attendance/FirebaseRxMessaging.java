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
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.eusecom.attendance.models.MessData;
import com.eusecom.attendance.models.Message;
import com.eusecom.attendance.models.NotifyData;

public class FirebaseRxMessaging {

    String to, title, body;
    private static final String TAG = RxjavaActivity.class.getSimpleName();
    private GitHubRepoAdapter adapter = new GitHubRepoAdapter();
    private Subscription subscription;


    public FirebaseRxMessaging(String to, String title, String body) {
        this.to= to;
        this.title = title;
        this.body = body;
    }


    public boolean SendNotification() {

        MessData messdata = new MessData("This is a GCM Topic Message!");
        NotifyData notifydata = new NotifyData(title,body);
        Message message = new Message(to, notifydata, "");

        subscription = FbmessClient.getInstance()
                .sendmyMessage("xxxxx", message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Message>() {
                    @Override public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "In onError()");
                    }

                    @Override public void onNext(Message message) {
                        Log.d(TAG, "In onNext()");
                        Log.d("message", message.getMessage_id());
                        //adapter.setGitHubRepos(gitHubRepos);
                    }
                });
        return true;
    }


}