package com.eusecom.attendance;

/**
 * send events by https://github.com/kaushikgopal/RxJava-Android-Samples
 * send pojo EventRxBus by https://quangson8128.github.io/2016/06/23/rxbus-android/
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.eusecom.attendance.fragments.RxbusMainFragment;
import com.eusecom.attendance.rxbus.RxBus;

public class RxbusActivity
      extends FragmentActivity {

    private RxBus _rxBus = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .replace(android.R.id.content, new RxbusMainFragment(), this.toString())
                                       .commit();
        }
    }

    // This is better done with a DI Library like Dagger
    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }

}