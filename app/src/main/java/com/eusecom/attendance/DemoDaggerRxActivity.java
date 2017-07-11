package com.eusecom.attendance;

import com.eusecom.attendance.models.Repository;
import com.eusecom.attendance.retrofit.GitHubApiInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.reactivestreams.Subscription;
import java.util.List;
import javax.inject.Inject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

//by https://github.com/codepath/dagger2-example
//edited to retrofit2 and okhttp3


public class DemoDaggerRxActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    GitHubApiInterface mGitHubApiInterface;

    private Subscription mSubscription;
    private Disposable searchDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daggertworxactivity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //public void onNext(List<Repository> repos)


                searchDisposable = mGitHubApiInterface.getRxRepository("codepath")
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Repository>>() {
                            @Override
                            public void accept(List<Repository> result) {
                                Log.i("NAME 0", result.get(1).getName());
                                Snackbar.make(view,"Retrieved 1 " + result.get(1).getName(), Snackbar.LENGTH_LONG)
                                        .setAction("Action",null).show();
                            }
                        });




            }



            });

        ((AttendanceApplication) getApplication()).getGitHubComponent().inject(this);
    }

    public void onDestroy() {
        super.onDestroy();


        searchDisposable.dispose();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demodaggertwomain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_rxretrofit) {

            Intent is = new Intent(getApplicationContext(), DemoDaggerRxActivity.class);
            startActivity(is);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
