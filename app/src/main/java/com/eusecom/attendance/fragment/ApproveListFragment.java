package com.eusecom.attendance.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.eusecom.attendance.Constants;
import com.eusecom.attendance.FbmessClient;
import com.eusecom.attendance.SettingsActivity;
import com.eusecom.attendance.animators.BaseItemAnimator;
import com.eusecom.attendance.animators.FadeInAnimator;
import com.eusecom.attendance.animators.FadeInDownAnimator;
import com.eusecom.attendance.animators.FadeInLeftAnimator;
import com.eusecom.attendance.animators.FadeInRightAnimator;
import com.eusecom.attendance.animators.FadeInUpAnimator;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.MessData;
import com.eusecom.attendance.models.Message;
import com.eusecom.attendance.models.NotifyData;
import com.eusecom.attendance.retrofit.RfContributor;
import com.eusecom.attendance.retrofit.RfEtestApi;
import com.eusecom.attendance.retrofit.RfEtestService;
import com.eusecom.attendance.retrofit.RfUser;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import static android.text.TextUtils.isEmpty;

public class ApproveListFragment extends Fragment {

    private static final String TAG = "ApproveListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    public ApproveListFragment() {}
    private ProgressDialog fProgressDialog;
    boolean isCancelable;
    String timestampx;

    private RfEtestApi _rfetestService;
    private CompositeDisposable _disposables;

    private Subscription subscription;

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

    private ApproveRxAdapter mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public ApproveListFragment.GetApproveSubscriber getApproveSubscriber;
    private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference gettimestramp = null;
    ValueEventListener getTimeListener = null;
    private RxBus _rxBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = Constants.ETEST_API_KEY;
        String urlx = "http:\\" + SettingsActivity.getServerName(getActivity());
        String usicox = SettingsActivity.getUsIco(getActivity());
        if( usicox.equals("44551142")) {
            urlx = "http:\\www.edcom.sk";
        }
        _rfetestService = RfEtestService.createGithubService(githubToken, urlx);

        _disposables = new CompositeDisposable();

        _rxBus = getRxBusSingleton();

        ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

        _disposables
                .add(tapEventEmitter.subscribe(event -> {
                    if (event instanceof ApproveListFragment.TapEvent) {
                        ///_showTapText();
                    }
                    if (event instanceof Attendance) {
                        String keys = ((Attendance) event).getRok();
                        //Log.d("In FRGM longClick", keys);
                        getApproveDialog( keys, (Attendance) event);

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
        View rootView = inflater.inflate(R.layout.fragment_approve, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.approve_list);
        mRecycler.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ApproveRxAdapter(Collections.<Attendance>emptyList(), _rxBus);
        getApproveSubscriber = new ApproveListFragment.GetApproveSubscriber();

        gettimestramp = FirebaseDatabase.getInstance().getReference("gettimestamp");
        getTimeListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                timestampx=dataSnapshot.getValue().toString();
                Log.d(TAG, "TIMESTAMP onActcreate " + timestampx);
            }
            public void onCancelled(DatabaseError databaseError) { }
        };

        gettimestramp.addValueEventListener(getTimeListener);
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
        loadAbsencesForApproving();




    }//end of onActivityCreated

    private void loadAbsencesForApproving() {

        final String companyIco = SettingsActivity.getUsIco(getActivity());
        Query recentAbsencesQuery = firebaseRef.child("company-absences").child(companyIco).orderByChild("aprv").equalTo("0")
                .limitToFirst(200);

        //showProgress(true);
        showfProgressDialog();
        RxFirebaseDatabase.getInstance().observeValueEvent(recentAbsencesQuery).subscribe(getApproveSubscriber);
    }

    private void renderApproveList(List<Attendance> blogPostEntities) {
        //showProgress(false);
        hidefProgressDialog();
        mAdapter.setData(blogPostEntities);
        //Log.d("blogPostEntities", blogPostEntities.get(0).getTitle());
        //Log.d("blogPostEntities", blogPostEntities.get(1).getTitle());
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        gettimestramp.removeEventListener(getTimeListener);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        _disposables.dispose();
        if (getApproveSubscriber != null && !getApproveSubscriber.isUnsubscribed()) {
            getApproveSubscriber.unsubscribe();
        }

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    // [START deletefan_out]
    private void approveAbsenceToServer(String postkey, int anodaj, Attendance model) {

        final String datsx = model.getDatsString();
        Log.d(TAG, "datsx " + datsx);
        final String dmxax = model.getDmxa();
        Log.d(TAG, "dmxax " + dmxax);
        final String daodx = model.getDaod();
        Log.d(TAG, "daodx " + daodx);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String approveabs_json = gson.toJson(model);
        String savetofir =  SettingsActivity.getFir(getActivity());
        String whoapprove =  SettingsActivity.getUsOsc(getActivity());

        _disposables.add(_rfetestService.contributors(savetofir, postkey, whoapprove, approveabs_json, anodaj)
                .flatMap(Observable::fromIterable)
                .flatMap(contributor -> {
                    Observable<RfUser> _userObservable = _rfetestService.user(savetofir, postkey, whoapprove, approveabs_json, anodaj, contributor.login)
                            .filter(user -> !isEmpty(user.name) && !isEmpty(user.email));

                    return Observable.zip(_userObservable,
                            Observable.just(contributor),
                            Pair::new);
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Pair<RfUser,RfContributor>>() {
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Retrofit call 2 completed ");
                        Toast.makeText(getActivity(), "Retrofit call 2 completed ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {
                        String snext = "Error " + e + getResources().getString(R.string.abs_savednot);
                        Toast.makeText(getActivity(), snext, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Pair pair) {
                        RfUser user = ((Pair<RfUser, RfContributor>)pair).first;
                        RfContributor contributor = ((Pair<RfUser, RfContributor>)pair).second;

                        String snext =  "";

                        String snextx =  " "  +  user.name + " "
                                + user.email + " "
                                + contributor.contributions + " "
                                + contributor.memo + " "
                                + contributor.saved;

                        if(contributor.saved == 1 ) {
                            snext = getResources().getString(R.string.abs_saved) + snextx;
                            approveAbsenceToFB(postkey, contributor.anodaj, model);
                        }else{
                            snext = getResources().getString(R.string.abs_savednot);
                            approveAbsenceToFB(postkey, contributor.anodaj, model);
                        }


                        Log.d(TAG, "onnext " + snext);
                        Toast.makeText(getActivity(), snext, Toast.LENGTH_LONG).show();
                    }
                }));

    }
    // [END delete_fan_out]

    private void getApproveDialog(String postkey, Attendance model) {

        //if savetofir > 0 then save to server
        String savetofir =  SettingsActivity.getFir(getActivity());
        int savetofiri = Integer.parseInt(savetofir);

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.approve_dialog);
        dialog.setTitle(R.string.item);
        // set the custom dialog components - text, image and button
        long timestampod = Long.parseLong(model.daod) * 1000L;
        String dateods = getDate(timestampod );

        long timestampdo = Long.parseLong(model.dado) * 1000L;
        String datedos = getDate(timestampdo );

        String textx = model.usname + " " + model.dmxa +  " " + model.dmna + " " + dateods + " / " + datedos;
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(textx);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_edit);

        Button buttonApprove = (Button) dialog.findViewById(R.id.buttonApprove);
        // if button is clicked, close the custom dialog
        buttonApprove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                if(savetofiri>0){
                    approveAbsenceToServer(postkey, 1, model);
                }else{
                    approveAbsenceToFB(postkey, 1, model);
                }
            }
        });
        Button buttonRefuse = (Button) dialog.findViewById(R.id.buttonRefuse);
        // if button is clicked, close the custom dialog
        buttonRefuse.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                if(savetofiri>0){
                    approveAbsenceToServer(postkey, 0, model);
                }else{
                    approveAbsenceToFB(postkey, 0, model);
                }


            }
        });
        dialog.show();

    }//end getdialog

    private void approveAbsenceToFB(String postkey, int anodaj, Attendance model) {

        //set appr at firebase child
        String apprx="1";
        if( anodaj == 1) {
            apprx="1";
        }else{
            apprx="2";
        }

        mDatabase.child("absences").child(postkey).child("aprv").setValue(apprx);
        mDatabase.child("user-absences").child(model.usid).child(postkey).child("aprv").setValue(apprx);
        mDatabase.child("company-absences").child(model.usico).child(postkey).child("aprv").setValue(apprx);

        //send notifycation
        //Toast.makeText(getActivity(), "Set approved=1", Toast.LENGTH_LONG).show();
        String Notititle = SettingsActivity.getUsname(getActivity()) + " "  + model.dmxa + " "  + model.dmna;
        long timestampod = Long.parseLong(model.daod) * 1000L;
        String datefroms = getDate(timestampod );
        long timestampdo = Long.parseLong(model.dado) * 1000L;
        String datetos = getDate(timestampdo );
        String Notibody ="";

        if( anodaj == 1 ) {
            Notibody = getString(R.string.isapproved) + " " + model.dmxa + " " + model.dmna + " "
                    + getString(R.string.from) + " " + datefroms + " " + getString(R.string.to) + " " + datetos
                    + " " + model.hodxb + " " + getString(R.string.hodiny);
        }else{
            Notibody = getString(R.string.isrefused) + " " + model.dmxa + " " + model.dmna + " "
                    + getString(R.string.from) + " " + datefroms + " " + getString(R.string.to) + " " + datetos
                    + " " + model.hodxb + " " + getString(R.string.hodiny);
        }

        //String approvetopic = "/topics/approve" + SettingsActivity.getUsIco(getActivity());
        String approvetopic = "/topics/mytopic" + model.usid;
        //FirebaseRetrofitMessaging firebasemessaging = new FirebaseRetrofitMessaging("/topics/news", Notititle, Notibody);
        FirebaseRxApproveMessaging firebasemessaging = new FirebaseRxApproveMessaging(approvetopic, Notititle, Notibody, anodaj);
        subscription = firebasemessaging.SendNotification();

    }

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


    public class FirebaseRxApproveMessaging {

        String to, title, body;
        int anodaj;
        private Subscription subscription;


        public FirebaseRxApproveMessaging(String to, String title, String body, int anodaj) {
            this.to = to;
            this.title = title;
            this.body = body;
            this.anodaj = anodaj;
        }


        public Subscription SendNotification() {

            MessData messdata = new MessData("This is a GCM Topic Message!");
            NotifyData notifydata = new NotifyData(title, body);
            Message message = new Message(to, notifydata, "");

            subscription = FbmessClient.getInstance()
                    .sendmyMessage("xxxxx", message)
                    .subscribeOn(rx.schedulers.Schedulers.io())
                    .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Message>() {
                        @Override
                        public void onCompleted() {

                            //hideProgressDialog();
                            String notis="";
                            if( anodaj == 1 ) {
                                notis = getString(R.string.isapproved);
                            }else{
                                notis = getString(R.string.isrefused);
                            }
                            //Log.d(TAG, "In onCompleted()");
                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setTitle(getString(R.string.absence))
                                    .setMessage(notis)
                                    .setPositiveButton(getString(R.string.textok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            //getActivity().finish();
                                        }
                                    })

                                    .show();

                            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                                @Override
                                public boolean onKey(DialogInterface arg0, int keyCode,
                                                     KeyEvent event) {
                                    // TODO Auto-generated method stub
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        //getActivity().finish();
                                        dialog.dismiss();
                                    }
                                    return true;
                                }
                            });
                        }


                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.d(TAG, "In onError()");
                        }

                        @Override
                        public void onNext(Message message) {
                            Log.d(TAG, "In onNext()");
                            Log.d("message", message.getMessage_id());
                            //adapter.setGitHubRepos(gitHubRepos);
                        }
                    });
            return subscription;
        }

    }//end of FirebaseRxApproveMessaging


    /**
     * Subscriber for {@link //RxFirebaseDatabase} query
     */
    private final class GetApproveSubscriber extends Subscriber<DataSnapshot> {
        @Override public void onCompleted() {
            //showProgress(false);
            hidefProgressDialog();
            getApproveSubscriber.unsubscribe();
        }

        @Override public void onError(Throwable e) {
            //showProgress(false);
            //showError(e.getMessage());
            hidefProgressDialog();
            getApproveSubscriber.unsubscribe();
        }

        @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
            List<Attendance> blogPostEntities = new ArrayList<>();
            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                String keys = childDataSnapshot.getKey();
                Log.d("keys ", keys);
                Attendance resultx = childDataSnapshot.getValue(Attendance.class);
                resultx.setRok(keys);
                blogPostEntities.add(resultx);
            }
            renderApproveList(blogPostEntities);

        }
    }//end of getApproveSubscriber

    public static class TapEvent {}


}
