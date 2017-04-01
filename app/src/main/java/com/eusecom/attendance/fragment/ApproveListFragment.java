package com.eusecom.attendance.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eusecom.attendance.Constants;
import com.eusecom.attendance.NewPostActivity;
import com.eusecom.attendance.SettingsActivity;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.DeletedAbs;
import com.eusecom.attendance.retrofit.RfContributor;
import com.eusecom.attendance.retrofit.RfEtestApi;
import com.eusecom.attendance.retrofit.RfEtestService;
import com.eusecom.attendance.retrofit.RfGithubApi;
import com.eusecom.attendance.retrofit.RfGithubService;
import com.eusecom.attendance.retrofit.RfUser;
import com.eusecom.attendance.viewholder.ApproveViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.eusecom.attendance.R;
import com.eusecom.attendance.models.Post;
import com.eusecom.attendance.viewholder.ApproveViewHolder;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;
import static java.lang.String.format;

public abstract class ApproveListFragment extends Fragment {

    private static final String TAG = "ApproveListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Attendance, ApproveViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public ApproveListFragment() {}
    String absxy;
    String abskeydel=null;
    private ProgressDialog fProgressDialog;
    boolean isCancelable, isrunning;
    String timestampx;

    private RfEtestApi _githubService;
    private CompositeDisposable _disposables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String githubToken = Constants.GITHUB_API_KEY;
        _githubService = RfEtestService.createGithubService(githubToken);

        _disposables = new CompositeDisposable();
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

        showfProgressDialog();

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("connected");
                    if(isrunning) { showfProgressDialog(); }
                } else {
                    System.out.println("not connected");
                    hidefProgressDialog();
                    if(isrunning) { Toast.makeText(getActivity(), getResources().getString(R.string.notconnected), Toast.LENGTH_SHORT).show(); }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

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

        // Set up FirebaseRecyclerAdapter with the Query
        Query absencesQuery = getQuery(mDatabase);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // the initial data has been loaded, hide the progress bar
                hidefProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

                hidefProgressDialog();
            }
        });



        mAdapter = new FirebaseRecyclerAdapter<Attendance, ApproveViewHolder>(Attendance.class, R.layout.item_absence,
                ApproveViewHolder.class, absencesQuery) {

            @Override
            protected void populateViewHolder(final ApproveViewHolder viewHolder, final Attendance model, final int position) {

                final DatabaseReference absRef = getRef(position);

                // Set click listener for the whole post view
                final String absKey = absRef.getKey();
                absxy = absRef.getKey();


                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Log.d(TAG, "onclick" + " listener");

                        //Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        //intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, absKey);

                            Toast.makeText(getActivity(), "Onclick " + absKey, Toast.LENGTH_SHORT).show();

                        //startActivity(intent);
                        }


                });

                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final String datsx = model.getDatsString();
                        //Log.d(TAG, "datsx " + datsx);

                        gettimestramp.setValue(ServerValue.TIMESTAMP);
                        //Log.d(TAG, "ServerValue.TIMESTAMP " + timestampx);

                        long timestampl = Long.parseLong(timestampx);
                        long datsl = Long.parseLong(datsx);
                        long rozdiel = timestampl - datsl;
                        //Log.d(TAG, "rozdiel " + rozdiel);

                        Toast.makeText(getActivity(), "Longclick " + absKey,Toast.LENGTH_SHORT).show();

                        abskeydel = absKey;

                        getDialog(abskeydel);


                        return true;
                    }


                });


                // Bind Abstype to ViewHolder
                viewHolder.bindToApprove(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(absRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);

                    }
                });
            }

        };

        mRecycler.setAdapter(mAdapter);

    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
        _disposables.dispose();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public abstract Query getQuery(DatabaseReference databaseReference);

    // [START deletefan_out]
    private void approvePost(String postkey, int anodaj) {

        _disposables.add(_githubService.contributors("square", "retrofit")
                .flatMap(Observable::fromIterable)
                .flatMap(contributor -> {
                    Observable<RfUser> _userObservable = _githubService.user(contributor.login)
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

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error while getting the list of contributors along with full " + "names");
                    }

                    @Override
                    public void onNext(Pair pair) {
                        RfUser user = ((Pair<RfUser, RfContributor>)pair).first;
                        RfContributor contributor = ((Pair<RfUser, RfContributor>)pair).second;


                        String snext =  " "  +  user.name + " "
                                + user.email + " "
                                + contributor.contributions;

                        Log.d(TAG, "onnext " + snext);
                    }
                }));

    }
    // [END delete_fan_out]

    private void getDialog(String postkey) {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.approve_dialog);
        dialog.setTitle(R.string.item);
        // set the custom dialog components - text, image and button
        String textx = getString(R.string.item) + " " + abskeydel;
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(textx);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_edit);

        Button buttonApprove = (Button) dialog.findViewById(R.id.buttonApprove);
        // if button is clicked, close the custom dialog
        buttonApprove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                approvePost(abskeydel, 1);
            }
        });
        Button buttonRefuse = (Button) dialog.findViewById(R.id.buttonRefuse);
        // if button is clicked, close the custom dialog
        buttonRefuse.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                approvePost(abskeydel, 0);


            }
        });
        dialog.show();

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



    @Override
    public void onResume() {
        super.onResume();
        isrunning=true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isrunning=false;
    }

}
