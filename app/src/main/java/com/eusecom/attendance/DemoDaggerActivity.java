package com.eusecom.attendance;

import com.eusecom.attendance.models.Repository;
import com.eusecom.attendance.retrofit.GitHubApiInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * by https://github.com/codepath/dagger2-example
 * edited to retrofit2 and okhttp3
 * scoped by https://guides.codepath.com/android/Dependency-Injection-with-Dagger-2
 *
 * You may see how Dagger do injection in generated class see in build/generated/source/apt/debug/com/...
 */


public class DemoDaggerActivity extends AppCompatActivity {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject
    Retrofit mRetrofit;

    @Inject
    GitHubApiInterface mGitHubApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daggertwoactivity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Call<ArrayList<Repository>> call = mGitHubApiInterface.getRepository("codepath");
                call.enqueue(new Callback<ArrayList<Repository>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Repository>> call, Response<ArrayList<Repository>> response) {

                        Log.i("DEBUG", response.body().toString());
                        Log.i("NAME 0", response.body().get(0).getName());

                        Snackbar.make(view,"Retrieved 0 " + response.body().get(0).getName(), Snackbar.LENGTH_LONG)
                                    .setAction("Action",null).show();


                    }

                    @Override
                    public void onFailure(Call<ArrayList<Repository>> call, Throwable t) {
                        Log.i("DEBUG ", "Failure");
                    }
                });
            }



            });

        ((AttendanceApplication) getApplication()).getGitHubComponent().inject(this);
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
