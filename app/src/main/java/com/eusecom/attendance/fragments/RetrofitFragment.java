package com.eusecom.attendance.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


import com.eusecom.attendance.Constants;
import com.eusecom.attendance.R;
import com.eusecom.attendance.retrofit.RfContributor;
import com.eusecom.attendance.retrofit.RfGithubApi;
import com.eusecom.attendance.retrofit.RfGithubService;
import com.eusecom.attendance.retrofit.RfUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


import static android.text.TextUtils.isEmpty;
import static java.lang.String.format;

public class RetrofitFragment extends Fragment {

    @Bind(R.id.demo_retrofit_contributors_username) EditText _username;
    @Bind(R.id.demo_retrofit_contributors_repository) EditText _repo;
    @Bind(R.id.log_list) ListView _resultList;

    private ArrayAdapter<String> _adapter;
    private RfGithubApi _githubService;
    private CompositeDisposable _disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = Constants.GITHUB_API_KEY;
        _githubService = RfGithubService.createGithubService(githubToken);

        _disposables = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_retrofit, container, false);
        ButterKnife.bind(this, layout);

        _adapter = new ArrayAdapter<>(getActivity(), R.layout.item_retrofitlog, R.id.item_log, new ArrayList<>());
        //_adapter.setNotifyOnChange(true);
        _resultList.setAdapter(_adapter);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _disposables.dispose();
    }

    @OnClick(R.id.btn_demo_retrofit_contributors)
    public void onListContributorsClicked() {
        _adapter.clear();

        _disposables.add(//
              _githubService.contributors(_username.getText().toString(), _repo.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<List<RfContributor>>() {

                        @Override
                        public void onComplete() {
                            //Timber.d("Retrofit call 1 completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            //Timber.e(e, "woops we got an error while getting the list of contributors");
                        }

                        @Override
                        public void onNext(List<RfContributor> contributors) {
                            for (RfContributor c : contributors) {
                                _adapter.add(format("%s has made %d contributions to %s",
                                      c.login,
                                      c.contributions,
                                      _repo.getText().toString()));

                                //Timber.d("%s has made %d contributions to %s",
                                //      c.login,
                                //      c.contributions,
                                //      _repo.getText().toString());
                            }
                        }
                    }));
    }

    @OnClick(R.id.btn_demo_retrofit_contributors_with_user_info)
    public void onListContributorsWithFullUserInfoClicked() {
        _adapter.clear();

        _disposables.add(_githubService.contributors(_username.getText().toString(), _repo.getText().toString())
              .flatMap(Observable::fromIterable)
              .flatMap(contributor -> {
                  Observable<RfUser> _userObservable = _githubService.user(contributor.login)
                        .filter(user -> !isEmpty(user.name) && !isEmpty(user.email));

                  return Observable.zip(_userObservable,
                        Observable.just(contributor),
                        Pair::new);
              })
              .subscribeOn(Schedulers.newThread())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeWith(new DisposableObserver<Pair<RfUser,RfContributor>>() {
                  @Override
                  public void onComplete() {
                      //Timber.d("Retrofit call 2 completed ");
                  }

                  @Override
                  public void onError(Throwable e) {
                      //Timber.e(e, "error while getting the list of contributors along with full " + "names");
                  }

                  @Override
                  public void onNext(Pair pair) {
                      RfUser user = ((Pair<RfUser, RfContributor>)pair).first;
                      RfContributor contributor = ((Pair<RfUser, RfContributor>)pair).second;

                      _adapter.add(format("%s(%s) has made %d contributions to %s",
                            user.name,
                            user.email,
                            contributor.contributions,
                            _repo.getText().toString()));

                      _adapter.notifyDataSetChanged();

                      //Timber.d("%s(%s) has made %d contributions to %s",
                      //      user.name,
                      //      user.email,
                      //      contributor.contributions,
                      //      _repo.getText().toString());
                  }
              }));
    }
}
