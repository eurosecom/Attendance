package com.eusecom.attendance;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
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


}
