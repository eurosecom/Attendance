/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eusecom.attendance;

import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.retrofit.AbsServerClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import rx.Subscription;

//by https://www.raywenderlich.com/141980/rxandroid-tutorial
//to find over List<Attendance>

//by https://www.toptal.com/android/functional-reactive-android-rxjava username=arriolac

public class AbsServerAsActivity extends AbsServerAsBaseSearchActivity {

  private Disposable mDisposable;
  private Subscription subscription;
  private TextWatcher watcher = null;
  private View.OnClickListener onclicklist = null;

  @Override
  protected void onStart() {
    super.onStart();

    Observable<String> buttonClickStream = createButtonClickObservable();
    Observable<String> textChangeStream = createTextChangeObservable();

    Observable<String> searchTextObservable = Observable.merge(textChangeStream, buttonClickStream);

    mDisposable = searchTextObservable // change this line
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext(new Consumer<String>() {
          @Override
          public void accept(String s) {
            showProgressBar();
          }
        })
        .observeOn(Schedulers.io())
        .map(new Function<String, List<Attendance>>() {
          @Override
          public List<Attendance> apply(String query) {
              // NullPointerException next row if i set search string and long click on item to dialog and then change orientation
            return mAbsServerSearchEngine.searchModel(query);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Attendance>>() {
          @Override
          public void accept(List<Attendance> result) {
            hideProgressBar();
            showResultAs(result);
          }
        });

      String getfromfir =  SettingsActivity.getFir(AbsServerAsActivity.this);
      getAbsServer(getfromfir);



  }//end onstart

   @Override
   public void onDestroy() {
        super.onDestroy();

       onclicklist = null;
       mSearchButton.setOnClickListener(null);
       mQueryEditText.removeTextChangedListener(watcher);
       mDisposable.dispose();
       if (subscription != null && !subscription.isUnsubscribed()) {
           subscription.unsubscribe();
       }
   }

  @Override
  protected void onStop() {
    super.onStop();

      onclicklist = null;
      mSearchButton.setOnClickListener(null);
      mQueryEditText.removeTextChangedListener(watcher);
      mDisposable.dispose();
      if (subscription != null && !subscription.isUnsubscribed()) {
          subscription.unsubscribe();
      }

  }


  @Override
  public void onBackPressed() {
      super.onBackPressed();

      onclicklist = null;
      mSearchButton.setOnClickListener(null);
      mQueryEditText.removeTextChangedListener(watcher);
      if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
      }
      if (!mDisposable.isDisposed()) {
      mDisposable.dispose();
      }

  }

  // 1
  private Observable<String> createButtonClickObservable() {

    // 2
    return Observable.create(new ObservableOnSubscribe<String>() {

      // 3
      @Override
      public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
        // 4
            onclicklist = new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  // 5
                  emitter.onNext(mQueryEditText.getText().toString());
              }
            };
            mSearchButton.setOnClickListener(onclicklist);

        // 6
        emitter.setCancellable(new Cancellable() {
          @Override
          public void cancel() throws Exception {
            // 7
              onclicklist = null;
              mSearchButton.setOnClickListener(null);
          }
        });
      }
    });
  }

  //1
  private Observable<String> createTextChangeObservable() {
    //2
    Observable<String> textChangeObservable = Observable.create(new ObservableOnSubscribe<String>() {
      @Override
      public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
        //3
        watcher = new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void afterTextChanged(Editable s) {}

          //4
          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            emitter.onNext(s.toString());
          }
        };

        //5
        mQueryEditText.addTextChangedListener(watcher);

        //6
        emitter.setCancellable(new Cancellable() {
          @Override
          public void cancel() throws Exception {
            mQueryEditText.removeTextChangedListener(watcher);
          }
        });
      }
    });

    return textChangeObservable
        .filter(new Predicate<String>() {
          @Override
          public boolean test(String query) throws Exception {
            return query.length() >= 2;
          }
        }).debounce(1000, TimeUnit.MILLISECONDS);  // add this line
  }

  private void getAbsServer(String fromfir) {
    showProgressBar();
      String urlx = SettingsActivity.getServerName(AbsServerAsActivity.this);
    subscription = AbsServerClient.getInstance(urlx)
            .getAbsServer(fromfir)
            .subscribeOn(rx.schedulers.Schedulers.io())
            .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(new rx.Observer<List<Attendance>>() {
              @Override public void onCompleted() {
                hideProgressBar();
                Log.d("", "In onCompleted()");
              }

              @Override public void onError(Throwable e) {
                e.printStackTrace();
                hideProgressBar();
                Log.d("", "In onError()");
              }

              @Override public void onNext(List<Attendance> absserverRepos) {
                Log.d("Thread onNext", Thread.currentThread().getName());
                //Log.d("absserverRepos", absserverRepos.get(0).getDmna());
                nastavResultAs(absserverRepos);
                showResultAs(absserverRepos);

              }
            });
  }




}
