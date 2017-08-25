package com.sqisland.android.test_demo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import javax.inject.Inject;


/*
* by http://blog.sqisland.com/2015/04/dagger-2-espresso-2-mockito.html
* github code https://github.com/chiuki/android-test-demo

*/

public class MainActivity extends AppCompatActivity {
  public static final String KEY_MILLIS = "millis";

  @Inject
  Clock clock;

  @Inject
  SharedPreferences mSharedPreferences;

  Toolbar mActionBarToolbar;
  TextView todayView, todayView2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ((DemoApplication) getApplication()).component().inject(this);

    mActionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
    setSupportActionBar(mActionBarToolbar);
    getSupportActionBar().setTitle(getString(R.string.app_name));

    todayView = (TextView) findViewById(R.id.date);
    todayView2 = (TextView) findViewById(R.id.date2);

    //long millis = getIntent().getLongExtra(KEY_MILLIS, -1);
    //DateTime dateTime = (millis > 0) ? new DateTime(millis) : clock.getNow();
    //todayView.setText(DateUtils.format(dateTime));

    String datetimes = clock.getNow().toString();
    todayView.setText(datetimes);

    String serverx = "From fragment " + mSharedPreferences.getString("servername", "");
    Toast.makeText(MainActivity.this, serverx, Toast.LENGTH_SHORT).show();


    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        String datetimes2 = clock.getNow().toString();
        todayView2.setText(datetimes2);

      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {


      case R.id.action_settings:
        startActivity(new Intent(this, SettingsActivity.class));
        //finish();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }


}