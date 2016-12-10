package com.eusecom.attendance.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.eusecom.attendance.NewPostActivity;
import com.eusecom.attendance.models.Absence;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.viewholder.AttendanceViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.eusecom.attendance.R;
import com.eusecom.attendance.models.Post;
import com.eusecom.attendance.viewholder.AbsenceViewHolder;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class AttendanceListFragment extends Fragment {

    private static final String TAG = "AbsenceListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Attendance, AttendanceViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    public AttendanceListFragment() {}
    String absxy;
    String abskeydel=null;
    private ProgressDialog fProgressDialog;
    boolean isCancelable, isrunning;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
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
                    if(isrunning) { Toast.makeText(getActivity(), "Not connected", Toast.LENGTH_SHORT).show(); }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });


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



        mAdapter = new FirebaseRecyclerAdapter<Attendance, AttendanceViewHolder>(Attendance.class, R.layout.item_absence,
                AttendanceViewHolder.class, absencesQuery) {

            @Override
            protected void populateViewHolder(final AttendanceViewHolder viewHolder, final Attendance model, final int position) {
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

                        Toast.makeText(getActivity(), "Longclick " + absKey,Toast.LENGTH_SHORT).show();

                        abskeydel = absKey;

                        getDialog(abskeydel);


                        return true;
                    }


                });


                // Bind Absence to ViewHolder
                viewHolder.bindToAttendance(model, new View.OnClickListener() {
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
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public abstract Query getQuery(DatabaseReference databaseReference);

    // [START deletefan_out]
    private void deletePost(String postkey) {

        // delete post key from /posts and user-posts/$userid simultaneously
        String userId = getUid();
        String key = postkey;


        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, null);
        childUpdates.put("/user-posts/" + userId + "/" + key, null);
        childUpdates.put("/post-comments/" + key, null);


        mDatabase.updateChildren(childUpdates);

    }
    // [END delete_fan_out]

    private void getDialog(String postkey) {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle(R.string.item);
        // set the custom dialog components - text, image and button
        String textx = getString(R.string.item) + " " + abskeydel;
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(textx);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_edit);

        Button buttonDelete = (Button) dialog.findViewById(R.id.buttonDelete);
        // if button is clicked, close the custom dialog
        buttonDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
                deletePost(abskeydel);
            }
        });
        Button buttonEdit = (Button) dialog.findViewById(R.id.buttonEdit);
        // if button is clicked, close the custom dialog
        buttonEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

                Intent i = new Intent(getActivity(), NewPostActivity.class);
                Bundle extras = new Bundle();
                extras.putString("editx", "1");
                extras.putString("keyx", abskeydel);

                i.putExtras(extras);
                startActivity(i);
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
