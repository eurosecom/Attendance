package com.eusecom.attendance.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.attendance.Constants;
import com.eusecom.attendance.SettingsActivity;
import com.eusecom.attendance.animators.BaseItemAnimator;
import com.eusecom.attendance.animators.FadeInAnimator;
import com.eusecom.attendance.animators.FadeInDownAnimator;
import com.eusecom.attendance.animators.FadeInLeftAnimator;
import com.eusecom.attendance.animators.FadeInRightAnimator;
import com.eusecom.attendance.animators.FadeInUpAnimator;
import com.eusecom.attendance.models.Abstype;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.eusecom.attendance.R;
import com.google.firebase.database.ValueEventListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import rx.Subscriber;


public class AbsTypesListRxFragment extends Fragment {

    private static final String TAG = "AbsTypesListRxFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    public AbsTypesListRxFragment() {}
    private ProgressDialog fProgressDialog;
    boolean isCancelable;
    String timestampx;

    private CompositeDisposable _disposables;

    enum Type {
        FadeIn(new FadeInAnimator()),
        FadeInDown(new FadeInDownAnimator()),
        FadeInUp(new FadeInUpAnimator()),
        FadeInLeft(new FadeInLeftAnimator()),
        FadeInRight(new FadeInRightAnimator());

        private BaseItemAnimator mAnimator;

        Type(BaseItemAnimator animator) {
            mAnimator = animator;
        }

        public BaseItemAnimator getAnimator() {
            return mAnimator;
        }
    }

    private AbsTypesRxAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public GetAbsTypesSubscriber getAbsTypesSubscriber;
    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private RxBus _rxBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = Constants.ETEST_API_KEY;
        String urlx = SettingsActivity.getServerName(getActivity());

        _disposables = new CompositeDisposable();

        _rxBus = getRxBusSingleton();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof AbsTypesListRxFragment.TapEvent) {
                        ///_showTapText();
                    }
                    if (event instanceof Abstype) {
                        String keys = "zzzz";
                        Log.d("In FRGM longClick", keys);
                        getAbsTypesDialog( keys, (Abstype) event);

                    }
                }));

        _disposables
                .add(tapEventEmitter.publish(stream ->
                        stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                            ///_showTapCount(taps.size()); OK
                        }));

        _disposables.add(tapEventEmitter.connect());
    }

    public RxBus getRxBusSingleton() {
        if (_rxBus == null) {
            _rxBus = new RxBus();
        }

        return _rxBus;
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_abstypes, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.abstypes_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new AbsTypesRxAdapter(Collections.<Abstype>emptyList(), _rxBus);
        getAbsTypesSubscriber = new GetAbsTypesSubscriber();

        DatabaseReference gettimestramp = FirebaseDatabase.getInstance().getReference("gettimestamp");
        gettimestramp.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.out.println(dataSnapshot.getValue());
                timestampx=dataSnapshot.getValue().toString();
                //Log.d(TAG, "ServerValue.TIMESTAMP " + timestampx);

            }

            public void onCancelled(DatabaseError databaseError) { }
        });
        gettimestramp.setValue(ServerValue.TIMESTAMP);


        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setItemAnimator(new FadeInRightAnimator());
        mRecycler.getItemAnimator().setAddDuration(300);
        mRecycler.getItemAnimator().setRemoveDuration(300);
        loadAbsTypes();


    }//end of onActivityCreated

    private void loadAbsTypes() {

        Query recentAbsencesQuery = firebaseRef.child("absencetypes").limitToFirst(200);

        showfProgressDialog();
        RxFirebaseDatabase.getInstance().observeValueEvent(recentAbsencesQuery).subscribe(getAbsTypesSubscriber);
    }

    private void renderAbsTypesList(List<Abstype> blogPostEntities) {
        //showProgress(false);
        hidefProgressDialog();
        mAdapter.setData(blogPostEntities);
        //Log.d("blogPostEntities", blogPostEntities.get(0).getTitle());
        //Log.d("blogPostEntities", blogPostEntities.get(1).getTitle());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        _disposables.dispose();
        if (getAbsTypesSubscriber != null && !getAbsTypesSubscriber.isUnsubscribed()) {
            getAbsTypesSubscriber.unsubscribe();
        }

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }




    private void getAbsTypesDialog(String postkey, Abstype model) {

        //if savetofir > 0 then save to server
        String savetofir =  SettingsActivity.getFir(getActivity());
        int savetofiri = Integer.parseInt(savetofir);

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.absences_dialog);
        dialog.setTitle(R.string.item);

        String textx =  model.idm +  " " + model.iname;
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(textx);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_edit);

        Button buttonPositive = (Button) dialog.findViewById(R.id.buttonPositive);
        // if button is clicked, close the custom dialog
        buttonPositive.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        Button buttonNegative = (Button) dialog.findViewById(R.id.buttonNegative);
        // if button is clicked, close the custom dialog
        buttonNegative.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

                Toast.makeText(getActivity(), getResources().getString(R.string.cantedititem), Toast.LENGTH_SHORT).show();

            }
        });
        dialog.show();

    }//end getdialog



    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


    public void showfProgressDialog() {
        if (fProgressDialog == null) {
            fProgressDialog = new ProgressDialog(getActivity());
            fProgressDialog.setCancelable(isCancelable);
            fProgressDialog.setMessage("Loading...");
        }

        fProgressDialog.show();
    }

    public void hidefProgressDialog() {
        if (fProgressDialog != null && fProgressDialog.isShowing()) {
            fProgressDialog.dismiss();
        }
    }




    /**
     * Subscriber for {@link //RxFirebaseDatabase} query
     */
    private final class GetAbsTypesSubscriber extends Subscriber<DataSnapshot> {
        @Override public void onCompleted() {
            //showProgress(false);
            hidefProgressDialog();
            getAbsTypesSubscriber.unsubscribe();
        }

        @Override public void onError(Throwable e) {
            //showProgress(false);
            //showError(e.getMessage());
            hidefProgressDialog();
            getAbsTypesSubscriber.unsubscribe();
        }

        @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
            List<Abstype> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                Log.d("keys ", keys);
                Abstype resultx = childDataSnapshot.getValue(Abstype.class);
                //resultx.setRok(keys);
                blogPostEntities.add(resultx);
            }
            renderAbsTypesList(blogPostEntities);

        }
    }//end of getAbsenceSubscriber

    public static class TapEvent {}


}
