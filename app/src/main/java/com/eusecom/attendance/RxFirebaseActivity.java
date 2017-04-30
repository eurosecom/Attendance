package com.eusecom.attendance;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link AppCompatActivity} for the main screen
 * by https://github.com/ezhome/Android-RxFirebase
 */
public class RxFirebaseActivity extends AppCompatActivity {

  private DatabaseReference mDatabase;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rxfirebase);

    mDatabase = FirebaseDatabase.getInstance().getReference();

    final FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(R.id.fragmentContainer, PostsFragment.newInstance());
    fragmentTransaction.commit();

  }//end oncreate

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    menu.clear();

      getMenuInflater().inflate(R.menu.options_rxfirebase_menu, menu);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.add) {

      String key = mDatabase.child("fireblog").push().getKey();
      BlogPostEntity postx = new BlogPostEntity("new author", "new title" );

      Map<String, Object> attValues = postx.toMap();

      Map<String, Object> childUpdates = new HashMap<>();

      childUpdates.put("/fireblog/" + key, attValues);

      mDatabase.updateChildren(childUpdates);


      return true;
    }

    if (id == R.id.del) {


      return true;
    }





    return super.onOptionsItemSelected(item);
  }


}
