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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.retrofit.AbsServerClient;
import com.eusecom.attendance.retrofit.RfEtestApi;
import com.eusecom.attendance.retrofit.RfEtestService;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Subscription;

public abstract class CompanyChooseBaseSearchActivity extends AppCompatActivity {

  protected CompanyChooseSearchEngine mCompanyChooseSearchEngine;
  protected EditText mQueryEditText;
  protected Button mSearchButton;
  private CompanyChooseAdapter mAdapter;
  private ProgressBar mProgressBar;
  Toolbar mActionBarToolbar;
  List<String> cheeses;
  private RxBus _rxBus;
  private CompositeDisposable _disposables;

  private RfEtestApi _githubService;
  private Subscription subscription;

  private View.OnClickListener onclicklistapprove = null;
  private View.OnClickListener onclicklistrefuse = null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.companychoose_activity);

    mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
    setSupportActionBar(mActionBarToolbar);
    getSupportActionBar().setTitle(getString(R.string.action_absmysql));

    mQueryEditText = (EditText) findViewById(R.id.query_edit_text);
    mSearchButton = (Button) findViewById(R.id.search_button);
    mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    String githubToken = Constants.ETEST_API_KEY;
    String urlx = SettingsActivity.getServerName(this);
    _githubService = RfEtestService.createGithubService(githubToken, urlx);

    cheeses = Arrays.asList(getResources().getStringArray(R.array.cheeses3));

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
              if (event instanceof Attendance) {

                saveIcoId(((Attendance) event).daod + " / " + ((Attendance) event).dado, ((Attendance) event));
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
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }

  }


  private void saveIcoId(String texttoast, Attendance model) {

    Toast.makeText(CompanyChooseBaseSearchActivity.this, texttoast,
            Toast.LENGTH_LONG).show();


  }//end saveAbsServer






  public static class OnItemClickEvent {}

}
