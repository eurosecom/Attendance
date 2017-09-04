package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.eusecom.attendance.realm.RealmEmployee;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;

        @Inject
        SharedPreferences mSharedPreferences;

        @Inject
        DgAllEmpsAbsMvvmViewModel mViewModel;

        @NonNull
        private CompositeSubscription mSubscription;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                ((AttendanceApplication) getApplication()).dgaeacomponent().inject(this);

                mSubscription = new CompositeSubscription();

                setContentView(R.layout.activity_map);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
        }

        @Override
        public void onDestroy() {
                super.onDestroy();

                mSubscription.unsubscribe();
                mSubscription.clear();

        }

        @Override
        public void onResume() {
                super.onResume();
                //bind();
        }

        @Override
        public void onPause() {
                super.onPause();
                unBind();
        }

        private void bind() {

                mSubscription.add(mViewModel.getObservableFBusersRealmEmployee()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(this::setRealmEmployees));

        }

        private void setRealmEmployees(@NonNull final List<RealmEmployee> realmemployees) {


        }

        private void unBind() {

                mSubscription.unsubscribe();
                mSubscription.clear();

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                bind();
                // Add a marker and move the camera.
                LatLng eurosecom25 = new LatLng(49.2026598, 18.7431749);
                mMap.addMarker(new MarkerOptions().position(eurosecom25).title("Marker for eurosecom25"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(eurosecom25));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eurosecom25, 12.0f));
        }
}