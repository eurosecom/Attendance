package com.eusecom.attendance.mvp;


import android.content.Intent;
import android.util.Log;

import com.eusecom.attendance.MainActivity;
import com.eusecom.attendance.SplashScreen;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MainMVP.ModelOps {

    // Presenter reference
    private MainMVP.RequiredPresenterOps mPresenter;
    private CompositeDisposable disposables = new CompositeDisposable();

    public MainModel(MainMVP.RequiredPresenterOps mPresenter) {
        this.mPresenter = mPresenter;
    }

    /**
     * Sent from {@link MainPresenter#onDestroy(boolean)}
     * Should stop/kill operations that could be running
     * and aren't needed anymore
     */
    @Override
    public void onDestroy() {
        // destroying actions
        Log.d("Thread Model onDestroy", Thread.currentThread().getName());
        disposables.clear();
    }

    // Insert Note in DB
    @Override
    public void addNote(Note note) {

        // data business logic
        Log.d("Thread addNote", Thread.currentThread().getName());
        DisposableObserver<Note> d = getDisposableObserverNote();

        getObservableNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d);

        disposables.add(d);

    }

    // Removes Note from DB
    @Override
    public void delNote(Note note) {
        // data business logic
        // ...
        mPresenter.onNoteRemoved(note);
    }


    //rxjava logic


    private Observable<Note> getObservableNote(Note noteadd) {
        return Observable.just(true).map(aBoolean -> {
            Note noteget = doLongOperationNote(noteadd);
            return noteget;
        });
    }

    /**
     * Observer handles the result through the 3 important actions:
     * onCompleted, onError, onNext
     */
    private DisposableObserver<Note> getDisposableObserverNote() {
        return new DisposableObserver<Note>() {

            @Override
            public void onComplete() {

                Log.d("Thread onComplete", Thread.currentThread().getName());
                onDestroy();
            }

            @Override
            public void onError(Throwable e) {
                Log.d("onError", e.toString());
            }

            @Override
            public void onNext(Note noteget) {
                Log.d("Thread onNext", Thread.currentThread().getName());
                //mPresenter.onNoteInserted(note); make Observable with Note
                mPresenter.onNoteInsertedNote(noteget);

            }
        };
    }


    private Note doLongOperationNote(Note noteadd) {

        Log.d("Thread doLongOp", Thread.currentThread().getName());
        Note noteget = new Note("xxx", "111");
        return noteget;
    }





}//endMainModel