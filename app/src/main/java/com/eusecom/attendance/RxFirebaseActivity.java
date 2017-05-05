package com.eusecom.attendance;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Subscriber;

/**
 * The {@link AppCompatActivity} for the main screen
 * by https://github.com/ezhome/Android-RxFirebase
 */
public class RxFirebaseActivity extends AppCompatActivity {

  private DatabaseReference mDatabase;
  public ProgressBar progressBar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rxfirebase);

    mDatabase = FirebaseDatabase.getInstance().getReference();
    progressBar = (ProgressBar) findViewById(R.id.progressBar);

    final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    fragmentTransaction.replace(R.id.fragmentContainer, PostsFragment.newInstance());
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

      showProgress(true);
      BlogPostEntity postx = new BlogPostEntity("new author rx", "new title rx" );
      addBlogPostRx(postx,0);

      return true;
    }

    if (id == R.id.del) {

      showProgress(true);
      BlogPostEntity postx = new BlogPostEntity(null,null );
      addBlogPostRx(postx,1);

      return true;
    }


    return super.onOptionsItemSelected(item);

  }//end create options


  private void addBlogPostRx(BlogPostEntity postx, int del) {

    //final DatabaseReference firebaseRef = null;
    if( del == 0 ) {
      final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("fireblog");
      RxFirebaseDatabase.getInstance().observeSetValuePush(firebaseRef, postx, del).subscribe(new SetPostsSubscriber());
    }else{
      final DatabaseReference firebaseRefdel = FirebaseDatabase.getInstance().getReference().child("fireblog").child("-Kj9eLMKoAAe2o8B8IiL");
      RxFirebaseDatabase.getInstance().observeDelValuePush(firebaseRefdel, postx, del).subscribe(new SetPostsSubscriber());
    }


  }//end of add BlogPostEntity


  private void addBlogPost(BlogPostEntity postx) {

    String key = mDatabase.child("fireblog").push().getKey();
    Map<String, Object> attValues = postx.toMap();
    Map<String, Object> childUpdates = new HashMap<>();
    childUpdates.put("/fireblog/" + key, attValues);
    mDatabase.updateChildren(childUpdates);

  }//end of add BlogPostEntity

  /**
   * Subscriber for {@link //RxFirebaseDatabase} query
   */
  private final class SetPostsSubscriber extends Subscriber<String> {
    @Override public void onCompleted() {
      showProgress(false);
      unsubscribe();
    }

    @Override public void onError(Throwable e) {
      showProgress(false);
      showError(e.getMessage());
    }

    @SuppressWarnings("unchecked") @Override public void onNext(String keyf) {
      showMessage(keyf);
    }
  }//end of setpostssubscriber

  private void showProgress(boolean isVisible) {
    this.progressBar.clearAnimation();
    this.progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    //this.getActivity().setProgressBarIndeterminateVisibility(isVisible); ??? App crashed by onDatachanged
  }

  private void showError(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  private void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }


}
