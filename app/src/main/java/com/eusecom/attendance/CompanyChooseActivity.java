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
import android.util.Log;
import android.view.View;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
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
import rx.Subscriber;

//by https://www.raywenderlich.com/141980/rxandroid-tutorial
//to find over List<Attendance>

//by https://www.toptal.com/android/functional-reactive-android-rxjava username=arriolac

public class CompanyChooseActivity extends CompanyChooseBaseSearchActivity {

  private Disposable mDisposable;
  private TextWatcher watcher = null;
  private View.OnClickListener onclicklist = null;
   public GetCompanySubscriber getCompanySubscriber;
   private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

  @Override
  protected void onStart() {
    super.onStart();

      getCompanySubscriber = new GetCompanySubscriber();
      loadCompanies();
      getObservableSearchText();

  }//end onstart

    private void loadCompanies() {

        Query recentCompaniesQuery = firebaseRef.child("companies").orderByChild("cmico");

        RxFirebaseDatabase.getInstance().observeValueEvent(recentCompaniesQuery).subscribe(getCompanySubscriber);
    }


    private void renderCompaniesList(List<Company> blogPostEntities) {

        //mAdapter.setData(blogPostEntities);
        Log.d("blogPostEntities", blogPostEntities.get(0).getCmname());
        //Log.d("blogPostEntities", blogPostEntities.get(1).getTitle());
        nastavResultCompany(blogPostEntities);
        showResultCompany(blogPostEntities);
    }


   private void getObservableSearchText() {
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
               .map(new Function<String, List<Company>>() {
                   @Override
                   public List<Company> apply(String query) {
                       return mCompanyChooseSearchEngine.searchModel(query);
                   }
               })
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Consumer<List<Company>>() {
                   @Override
                   public void accept(List<Company> result) {
                       hideProgressBar();
                       showResultCompany(result);
                   }
               });
   }

   @Override
   public void onDestroy() {
        super.onDestroy();

       Log.d("ondestroy ", "absserverasactivity");
       onclicklist = null;
       mSearchButton.setOnClickListener(null);
       mQueryEditText.removeTextChangedListener(watcher);
       getCompanySubscriber.unsubscribe();

   }

  @Override
  protected void onStop() {
    super.onStop();

      onclicklist = null;
      mSearchButton.setOnClickListener(null);
      mQueryEditText.removeTextChangedListener(watcher);
      getCompanySubscriber.unsubscribe();

  }


  @Override
  public void onBackPressed() {
      super.onBackPressed();

      onclicklist = null;
      mSearchButton.setOnClickListener(null);
      mQueryEditText.removeTextChangedListener(watcher);
      getCompanySubscriber.unsubscribe();

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
            return query.length() >= 3;
          }
        }).debounce(1000, TimeUnit.MILLISECONDS);  // add this line
  }


    private final class GetCompanySubscriber extends Subscriber<DataSnapshot> {
        @Override public void onCompleted() {

            getCompanySubscriber.unsubscribe();
        }

        @Override public void onError(Throwable e) {

            getCompanySubscriber.unsubscribe();
        }

        @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
            List<Company> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                //Log.d("keys ", keys);
                Company resultx = childDataSnapshot.getValue(Company.class);
                //resultx.setRok(keys);
                blogPostEntities.add(resultx);
            }
            renderCompaniesList(blogPostEntities);

        }
    }//end of getCompanySubscriber



}
