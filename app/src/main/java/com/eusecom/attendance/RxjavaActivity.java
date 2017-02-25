package com.eusecom.attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.eusecom.attendance.models.MessData;
import com.eusecom.attendance.models.Message;
import com.eusecom.attendance.models.NotifyData;

import java.util.List;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//by https://www.toptal.com/android/functional-reactive-android-rxjava

public class RxjavaActivity extends AppCompatActivity {

    private static final String TAG = RxjavaActivity.class.getSimpleName();
    private GitHubRepoAdapter adapter = new GitHubRepoAdapter();
    private Subscription subscription;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        final ListView listView = (ListView) findViewById(R.id.list_view_repos);
        listView.setAdapter(adapter);

        final EditText editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        final Button buttonSearch = (Button) findViewById(R.id.button_search);
        editTextUsername.setText("arriolac");
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                final String username = editTextUsername.getText().toString();

                if (!TextUtils.isEmpty(username)) {
                    //getStarredRepos(username);

                    MessData messdata = new MessData("This is a GCM Topic Message!");
                    NotifyData notifydata = new NotifyData("title","body");
                    sendMessage(username, new Message("/topics/news", notifydata, ""));
                }
            }
        });
    }

    @Override protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void sendMessage(String username, Message message) {
        subscription = FbmessClient.getInstance()
                .sendmyMessage(username, message)
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
    }

    private void getStarredRepos(String username) {
        subscription = GitHubClient.getInstance()
                                   .getStarredRepos(username)
                                   .subscribeOn(Schedulers.io())
                                   .observeOn(AndroidSchedulers.mainThread())
                                   .subscribe(new Observer<List<GitHubRepo>>() {
                                       @Override public void onCompleted() {
                                           Log.d(TAG, "In onCompleted()");
                                       }

                                       @Override public void onError(Throwable e) {
                                           e.printStackTrace();
                                           Log.d(TAG, "In onError()");
                                       }

                                       @Override public void onNext(List<GitHubRepo> gitHubRepos) {
                                           Log.d(TAG, "In onNext()");
                                           adapter.setGitHubRepos(gitHubRepos);
                                       }
                                   });
    }
}
