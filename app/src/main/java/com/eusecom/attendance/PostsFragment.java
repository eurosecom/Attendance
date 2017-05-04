package com.eusecom.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.eusecom.attendance.BlogPostsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.Subscriber;

/**
 * The {@link Fragment} to show a list with posts
 */
public class PostsFragment extends Fragment {

  //For example purposes
  private final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference();

  //Adapter
  private BlogPostsAdapter blogPostsAdapter;

  public RecyclerView rvPostsList;
  public ProgressBar progressBar;
  private LinearLayoutManager mManager;
  public PostsFragment() {}

  /**
   * Factory method to instantiate Fragment
   *
   * @return {@link PostsFragment}
   */
  public static PostsFragment newInstance() {
    return new PostsFragment();
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    //View fragmentView = inflater.inflate(R.layout.fragment_rxfirebase_posts, container, false);
    View fragmentView = inflater.inflate(R.layout.fragment_rxfirebase, container, false);


    //rvPostsList = (RecyclerView) fragmentView.findViewById(R.id.rvPostsList);
    //progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);
    rvPostsList = (RecyclerView) fragmentView.findViewById(R.id.approve_list);
    rvPostsList.setHasFixedSize(true);

    progressBar = (ProgressBar) fragmentView.findViewById(R.id.progressBar);

    return fragmentView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    blogPostsAdapter = new BlogPostsAdapter(Collections.<BlogPostEntity>emptyList());

    mManager = new LinearLayoutManager(getActivity());
    mManager.setReverseLayout(true);
    mManager.setStackFromEnd(true);
    rvPostsList.setLayoutManager(mManager);
    rvPostsList.setAdapter(blogPostsAdapter);
    loadPosts();

  }



  /**
   * Load the posts
   */
  private void loadPosts() {

    Query fbQuery = firebaseRef.child("fireblog");
    showProgress(true);
    RxFirebaseDatabase.getInstance().observeValueEvent(fbQuery).subscribe(new GetPostsSubscriber());
  }

  /**
   * Renders in UI the available {@link BlogPostEntity}
   *
   * @param blogPostEntities {@link List} of {@link BlogPostEntity}
   */
  private void renderBlogPosts(List<BlogPostEntity> blogPostEntities) {
    showProgress(false);
    blogPostsAdapter.setData(blogPostEntities);
    //Log.d("blogPostEntities", blogPostEntities.get(0).getTitle());
    //Log.d("blogPostEntities", blogPostEntities.get(1).getTitle());
  }

  /**
   * Shows the progress bar
   *
   * @param isVisible {@link Boolean} VISIBLE: true | INVISIBLE: false
   */
  private void showProgress(boolean isVisible) {
    progressBar.clearAnimation();
    progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
   //this.getActivity().setProgressBarIndeterminateVisibility(isVisible); ??? App crashed by onDatachanged
  }

  /**
   * Shows the error in UI
   *
   * @param message {@link String} the error message
   */
  private void showError(String message) {
    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
  }

  /**
   * Subscriber for {@link //RxFirebaseDatabase} query
   */
  private final class GetPostsSubscriber extends Subscriber<DataSnapshot> {
    @Override public void onCompleted() {
      showProgress(false);
      unsubscribe();
    }

    @Override public void onError(Throwable e) {
      showProgress(false);
      showError(e.getMessage());
    }

    @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
      List<BlogPostEntity> blogPostEntities = new ArrayList<>();
      for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
        blogPostEntities.add(childDataSnapshot.getValue(BlogPostEntity.class));
      }
      renderBlogPosts(blogPostEntities);
    }
  }
}
