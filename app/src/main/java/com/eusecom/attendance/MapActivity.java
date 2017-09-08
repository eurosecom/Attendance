package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import com.eusecom.attendance.models.Employee;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

        int lenmoje=1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                ((AttendanceApplication) getApplication()).dgaeacomponent().inject(this);

                String ustype = SettingsActivity.getUsType(this);
                if (ustype.equals("99")) {
                        lenmoje=0;
                }else{

                }

                mSubscription = new CompositeSubscription();

                setContentView(R.layout.activity_map);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                bind();

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

                mSubscription.add(mViewModel.getObservableFbEmployeeAtWork()
                        .subscribeOn(Schedulers.computation())
                        .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe(this::setEmployeesAtWork));

        }

        private void setEmployeesAtWork(@NonNull final List<Employee> employees) {

                double maxdist=0; double prevlatid = 0; double prevlongid = 0; float mapZoomLevel = 7f; LatLng newloc = null; int pocx=0;

                for (Employee employee : employees) {
                        //System.out.println("name " + employee.getUsername());

                        if (employee.getUsatw().equals("1")) {

                                double latid = Double.parseDouble(employee.getLati());
                                double longid = Double.parseDouble(employee.getLongi());

                                if (prevlatid == 0) {
                                        prevlatid = latid;
                                }
                                if (prevlongid == 0) {
                                        prevlongid = longid;
                                }

                                // Add a marker and move the camera.
                                newloc = new LatLng(latid, longid);
                                mMap.addMarker(new MarkerOptions().position(newloc).title(employee.getUsername()));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(eurosecom25));

                                //double dist=1;
                                double dist = distance(latid, longid, prevlatid, prevlongid);
                                if (dist > maxdist) {
                                        maxdist = dist;
                                }


                                System.out.println("name " + employee.getUsername() + " lati " + latid + " longi " + longid);
                                System.out.println("name " + employee.getUsername() + " prevlati " + prevlatid + " prevlongi " + prevlongid);
                                System.out.println("name " + employee.getUsername() + " dist " + dist + " maxdist " + maxdist);

                                pocx=pocx+1;
                        }

                }

                mapZoomLevel = getzoomlevel(maxdist);
                System.out.println("name maxdist " + maxdist + " mapZoomLevel " + mapZoomLevel);
                if( pocx > 0 ) { mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newloc, mapZoomLevel)); }


        }

        private float getzoomlevel(double dist) {

                float mapZoomLevel = 7;
                if (dist > 0 && dist <= 5) {
                        mapZoomLevel = 11;
                } else if (dist > 5 && dist <= 10) {
                        mapZoomLevel = 10;
                } else if (dist > 10 && dist <= 20) {
                        mapZoomLevel = 9;
                } else if (dist > 20 && dist <= 40) {
                        mapZoomLevel = 8;
                } else if (dist > 40 && dist < 100) {
                        mapZoomLevel = 7;
                } else if (dist > 100 && dist < 200) {
                        mapZoomLevel = 6;
                } else if (dist > 200 && dist < 400) {
                        mapZoomLevel = 5;
                } else if (dist > 400 && dist < 700) {
                        mapZoomLevel = 4;
                } else if (dist > 700 && dist < 1000) {
                        mapZoomLevel = 3;
                } else if (dist > 1000) {
                        mapZoomLevel = 2;
                } else {
                        mapZoomLevel = 14;
                }
                return mapZoomLevel;
        }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
                double theta = lon1 - lon2;
                double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515;
                return (dist);
        }
        private double deg2rad(double deg) {
                return (deg * Math.PI / 180.0);
        }
        private double rad2deg(double rad) {
                return (rad * 180.0 / Math.PI);
        }

        private void unBind() {

                mSubscription.unsubscribe();
                mSubscription.clear();

        }

}