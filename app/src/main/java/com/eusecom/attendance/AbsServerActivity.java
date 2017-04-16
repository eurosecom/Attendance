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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

//by https://www.raywenderlich.com/141980/rxandroid-tutorial

public class AbsServerActivity extends CheeseBaseSearchActivity {

  private Disposable mDisposable;

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
        .map(new Function<String, List<String>>() {
          @Override
          public List<String> apply(String query) {
            return mCheeseSearchEngine.search(query);
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<String>>() {
          @Override
          public void accept(List<String> result) {
            hideProgressBar();
            showResult(result);
          }
        });
  }

  @Override
  protected void onStop() {
    super.onStop();
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
        mSearchButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // 5
            emitter.onNext(mQueryEditText.getText().toString());
          }
        });

        // 6
        emitter.setCancellable(new Cancellable() {
          @Override
          public void cancel() throws Exception {
            // 7
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
        final TextWatcher watcher = new TextWatcher() {
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
}
