package com.eusecom.attendance;

/**
 * send events by https://github.com/kaushikgopal/RxJava-Android-Samples
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.eusecom.attendance.fragments.RxzipMainFragment;

public class RxzipActivity extends FragmentActivity {



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(android.R.id.content, new RxzipMainFragment(), this.toString())
                                       .commit();
        }
    }



}