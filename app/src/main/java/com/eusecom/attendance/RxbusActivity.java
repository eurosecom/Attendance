package com.eusecom.attendance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.eusecom.attendance.fragments.MainFragment;
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
                                       .replace(android.R.id.content, new MainFragment(), this.toString())
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