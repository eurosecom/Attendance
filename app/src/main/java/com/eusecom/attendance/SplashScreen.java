package com.eusecom.attendance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends Activity {

    private final int SPLASH_TIME_OUT = 1000;
    private FirebaseAuth mAuth;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        DisposableObserver<Boolean> d = getDisposableObserver();

        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d);

        disposables.add(d);

    }//end oncreate

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }


    private Observable<Boolean> getObservable() {
        return Observable.just(true).map(aBoolean -> {
            doLongOperation();
            return aBoolean;
        });
    }

    /**
     * Observer handles the result through the 3 important actions:
     * onCompleted, onError, onNext
     */
    private DisposableObserver<Boolean> getDisposableObserver() {
        return new DisposableObserver<Boolean>() {

            @Override
            public void onComplete() {

                Log.d("onComplete", " in");
                Log.d("SplashScreen", " Start MainActivity");
                Intent i = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onError(Throwable e) {
                Log.d("onError", e.toString());
            }

            @Override
            public void onNext(Boolean bool) {
                Log.d("onNext", " in");
            }
        };
    }


    private void doLongOperation() {

        try {
            mAuth = FirebaseAuth.getInstance();
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
    }



}