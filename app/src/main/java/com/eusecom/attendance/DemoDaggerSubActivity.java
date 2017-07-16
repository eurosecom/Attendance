package com.eusecom.attendance;

import com.eusecom.attendance.dagger.components.MyActivitySubComponent;
import com.eusecom.attendance.dagger.modules.MyActivityModule;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * Dagger2 demo classic Retrofit with Dependent Component GitHubComponent ( is dependent on NetComponent )
 * and with @Named @Provides @Named("cached") OkHttpClient okHttpClient(); and @Named("non_cached") OkHttpClient okHttpClientNonCached();
 *
 * by https://github.com/codepath/dagger2-example
 * edited to retrofit2 and okhttp3
 * scoped by https://guides.codepath.com/android/Dependency-Injection-with-Dagger-2
 *
 * You may see how Dagger do injection in generated class see in build/generated/source/apt/debug/com/...
 */


public class DemoDaggerSubActivity extends ListActivity {

    @Inject
    SharedPreferences mSharedPreferences;

    @Inject @Named("my_list")
    ArrayAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daggersubactivity_main);

        //old dagger subcomponent
        //((AttendanceApplication) getApplication()).getApplicationComponent().inject(this);
        //((AttendanceApplication) getApplication()).getApplicationComponent().newMyActivitySubcomponent(new MyActivityModule(this)).inject(this);

        //new dagger v2.7 subcomponent builder
        MyActivitySubComponent.Builder builder = (MyActivitySubComponent.Builder)
                ((AttendanceApplication) getApplication()).getApplicationComponent()
        .subcomponentBuilders()
                .get(MyActivitySubComponent.Builder.class)
                .get();
        builder.activityModule(new MyActivityModule(this)).build().inject(this);

        setListAdapter(mAdapter);
        mAdapter.add("xxxx");
        mAdapter.add("aaa");
        mAdapter.add("bbb");
        mAdapter.notifyDataSetChanged();

        String serverx = mSharedPreferences.getString("servername", "");

        Toast.makeText(DemoDaggerSubActivity.this, serverx, Toast.LENGTH_SHORT).show();


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


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
