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

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Post;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

import static java.lang.String.valueOf;

public abstract class CompanyChooseBaseSearchActivity extends AppCompatActivity {

  protected CompanyChooseSearchEngine mCompanyChooseSearchEngine;
  protected EditText mQueryEditText;
  protected Button mSearchButton;
  private CompanyChooseAdapter mAdapter;
  private ProgressBar mProgressBar;
  Toolbar mActionBarToolbar;
  private RxBus _rxBus;
  private CompositeDisposable _disposables;
  private DatabaseReference mDatabase;

  private View.OnClickListener onclicklistapprove = null;
  private View.OnClickListener onclicklistrefuse = null;

  @NonNull
  private CompositeSubscription mSubscription;
  AlertDialog dialog = null;
  AlertDialog.Builder builder = null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.companychoose_activity);

    mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
    setSupportActionBar(mActionBarToolbar);
    getSupportActionBar().setTitle(getString(R.string.choosecompany));

    mSubscription = new CompositeSubscription();
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(v -> {

      mSubscription.add(getNewCompanyDialog(getString(R.string.newcompany), getString(R.string.fullfirma))
              .subscribeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
              .observeOn(Schedulers.computation())
              .subscribe(this::setBoolean)
      );

            }
    );

    mQueryEditText = (EditText) findViewById(R.id.query_edit_text);
    mSearchButton = (Button) findViewById(R.id.search_button);
    mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    _rxBus = getRxBusSingleton();

    RecyclerView list = (RecyclerView) findViewById(R.id.list);
    list.setLayoutManager(new LinearLayoutManager(this));
    list.setAdapter(mAdapter = new CompanyChooseAdapter(_rxBus));

    _disposables = new CompositeDisposable();

    ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

    _disposables
            .add(tapEventEmitter.subscribe(event -> {
              if (event instanceof CompanyChooseBaseSearchActivity.OnItemClickEvent) {

              }
              if (event instanceof Company) {
                Company model = (Company) event;
                saveIcoId(model.cmico+ " " + model.cmname, model);

              }
            }));

    _disposables
            .add(tapEventEmitter.publish(stream ->
                    stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                      ///_showTapCount(taps.size()); OK
                    }));

    _disposables.add(tapEventEmitter.connect());
  }

  protected void showProgressBar() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  protected void hideProgressBar() {
    mProgressBar.setVisibility(View.GONE);
  }

  private void setBoolean(@NonNull final Boolean booleanx) {
    Log.i("setBoolean ", valueOf(booleanx));
  }

  protected void showResultCompany(List<Company> resultAs) {

    if (resultAs.isEmpty()) {
      Toast.makeText(this, R.string.nothing_found, Toast.LENGTH_SHORT).show();
      mAdapter.setCompany(Collections.<Company>emptyList());
    } else {
      //Log.d("showResultAs ", resultAs.get(0).dmna);
      mAdapter.setCompany(resultAs);
    }
  }


  protected void nastavResultCompany(List<Company> resultAs) {
    mCompanyChooseSearchEngine = new CompanyChooseSearchEngine(resultAs);
  }


  public RxBus getRxBusSingleton() {
    if (_rxBus == null) {
      _rxBus = new RxBus();
    }

    return _rxBus;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    Log.d("ondestroy ", "companychooseActivity");
    _disposables.clear();
    mSubscription.clear();

  }


    private void saveIcoId(String texttoast, Company model) {

    mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    mDatabase.child("users").child(user.getUid()).child("usico").setValue(model.cmico);
    mDatabase.child("users").child(user.getUid()).child("ustype").setValue("0");

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("usico", model.cmico).apply();
    editor.putString("ustype", "0").apply();
    editor.commit();

    Toast.makeText(CompanyChooseBaseSearchActivity.this, texttoast,
    Toast.LENGTH_LONG).show();


    finish();


  }//end saveIcoid


  private void createIco(Company savecompany) {

    mDatabase = FirebaseDatabase.getInstance().getReference();
    Query myTopPostsQuery = mDatabase.child("companies").child(savecompany.cmico);
    myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        // the initial data has been loaded, hide the progress bar

        Company result = dataSnapshot.getValue(Company.class);
          if( result != null ) {
            String texttoast = getString(R.string.companyexist, savecompany.cmico);
            //String texttoast = "New Company did not create. Company " + savecompany.cmico + " already exist.";
            Toast.makeText(CompanyChooseBaseSearchActivity.this, texttoast, Toast.LENGTH_LONG).show();
          }else{
            //Log.d("callsaveIco ", savecompany.cmico + " " + savecompany.cmname + " " + savecompany.cmcity + " " + savecompany.cmakt + " " + savecompany.cmdom + " " + savecompany.cmfir);
            saveNewCompany(savecompany);
          }
       }

      @Override
      public void onCancelled(DatabaseError firebaseError) {

      }
    });

  }//end createIcoid

  private void saveNewCompany(Company savecompany) {

    //Log.d("callsaveNew ", savecompany.cmico + " " + savecompany.cmname + " " + savecompany.cmcity + " " + savecompany.cmakt + " " + savecompany.cmdom + " " + savecompany.cmfir);

    mDatabase = FirebaseDatabase.getInstance().getReference();

    Map<String, Object> attValues = savecompany.toMap();
    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/companies/" + savecompany.cmico, attValues);
    mDatabase.updateChildren(childUpdates);


    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    mDatabase.child("users").child(user.getUid()).child("usico").setValue(savecompany.cmico);
    mDatabase.child("users").child(user.getUid()).child("ustype").setValue("99");

    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString("usico", savecompany.cmico).apply();
    editor.commit();

    //String texttoast = "New Company " + savecompany.cmico + " has created. ";
    String texttoast = getString(R.string.companycreated, savecompany.cmico);
    Toast.makeText(CompanyChooseBaseSearchActivity.this, texttoast, Toast.LENGTH_LONG).show();

    finish();

  }//end saveNewCompany

  Observable<Boolean> getNewCompanyDialog(String title, String message) {

    return Observable.create((Subscriber<? super Boolean> subscriber) -> {

      LayoutInflater inflater = this.getLayoutInflater();
      View textenter = inflater.inflate(R.layout.companies_new_dialog, null);
      final EditText namex = (EditText) textenter.findViewById(R.id.namex);
      namex.setText("name");
      final EditText icox = (EditText) textenter.findViewById(R.id.icox);
      icox.setText("12345678");
      final EditText cityx = (EditText) textenter.findViewById(R.id.cityx);
      cityx.setText("city");

      dialog = new AlertDialog.Builder(this)
              .setView(textenter)
              .setTitle(title)
              //.setMessage(message)
              .setPositiveButton(getString(R.string.save), (dialog, which) -> {

                String namexx =  namex.getText().toString();
                String icoxx =  icox.getText().toString();
                String cityxx =  cityx.getText().toString();

                Company newCompany = new Company(icoxx, namexx, " ", "0", cityxx);

                createIco(newCompany);

                try {
                  subscriber.onNext(true);
                  subscriber.onCompleted();
                } catch (Exception e) {
                  subscriber.onError(e);
                  e.printStackTrace();
                }
              })
              .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                try {
                  subscriber.onNext(false);
                  subscriber.onCompleted();
                } catch (Exception e) {
                  subscriber.onError(e);
                  e.printStackTrace();
                }
              })
              .create();
      // cleaning up
      subscriber.add(Subscriptions.create(dialog::dismiss));
      //textenter = null;
      dialog.show();

    });
  }






  public static class OnItemClickEvent {}

}
