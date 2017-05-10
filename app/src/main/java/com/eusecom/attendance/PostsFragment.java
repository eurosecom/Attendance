package com.eusecom.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.eusecom.attendance.animators.BaseItemAnimator;
import com.eusecom.attendance.animators.FadeInAnimator;
import com.eusecom.attendance.animators.FadeInDownAnimator;
import com.eusecom.attendance.animators.FadeInLeftAnimator;
import com.eusecom.attendance.animators.FadeInRightAnimator;
import com.eusecom.attendance.animators.FadeInUpAnimator;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2.database.RxFirebaseDatabase;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.flowables.ConnectableFlowable;
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
  public GetPostsSubscriber getPostsSubscriber;
  public SetPostsSubscriber setPostsSubscriber;
  private RxBus _rxBus;
  private CompositeDisposable _disposables;
  FloatingActionButton fab;
  int kolko=0;

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

  /**
   * Factory method to instantiate Fragment
   *
   * @return {@link PostsFragment}
   */
  public static PostsFragment newInstance() {
    return new PostsFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    _rxBus = getRxBusSingleton();

    _disposables = new CompositeDisposable();

    ConnectableFlowable<Object> tapEventEmitter = _rxBus.asFlowable().publish();

    _disposables
            .add(tapEventEmitter.subscribe(event -> {
              if (event instanceof PostsFragment.TapEvent) {
                ///_showTapText();
              }
              if (event instanceof BlogPostEntity) {
                //saveAbsServer(((Attendance) event).daod + " / " + ((Attendance) event).dado, ((Attendance) event));
                String keys = ((BlogPostEntity) event).getAuthor();
                //blogPostsAdapter.remove(0);
                showProgress(true);
                Log.d("In FRGM shortClick", keys);
                BlogPostEntity postx = new BlogPostEntity(null, null, null);
                delBlogPostRx(postx,1, keys);

              }
            }));

    _disposables
            .add(tapEventEmitter.publish(stream ->
                    stream.buffer(stream.debounce(1, TimeUnit.SECONDS)))
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(taps -> {
                      ///_showTapCount(taps.size()); OK
                    }));

    _disposables.add(tapEventEmitter.connect());

  }//end oncreate

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

    fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      showProgress(true);
      BlogPostEntity postx = new BlogPostEntity("new author rx", "new title rx", "0" );
      int posxx = blogPostsAdapter.getItemCount();
      blogPostsAdapter.add(postx, posxx);
      addBlogPostRx(postx,0);

      }
    });

    return fragmentView;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    blogPostsAdapter = new BlogPostsAdapter(Collections.<BlogPostEntity>emptyList(), _rxBus);
    getPostsSubscriber = new GetPostsSubscriber();
    setPostsSubscriber = new SetPostsSubscriber();

    mManager = new LinearLayoutManager(getActivity());
    //mManager.setReverseLayout(true);
    //mManager.setStackFromEnd(true);
    rvPostsList.setLayoutManager(mManager);
    rvPostsList.setAdapter(blogPostsAdapter);
    rvPostsList.setItemAnimator(new FadeInRightAnimator());
    rvPostsList.getItemAnimator().setAddDuration(300);
    rvPostsList.getItemAnimator().setRemoveDuration(300);
    loadPosts();

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    _disposables.clear();
    getPostsSubscriber.unsubscribe();
    setPostsSubscriber.unsubscribe();

  }

  public RxBus getRxBusSingleton() {
    if (_rxBus == null) {
      _rxBus = new RxBus();
    }

    return _rxBus;
  }

  /**
   * Load the posts
   */
  private void loadPosts() {

    Query fbQuery = firebaseRef.child("fireblog");
    showProgress(true);
    RxFirebaseDatabase.getInstance().observeValueEventDelayed(fbQuery).subscribe(getPostsSubscriber);
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
      getPostsSubscriber.unsubscribe();
    }

    @Override public void onError(Throwable e) {
      showProgress(false);
      showError(e.getMessage());
      getPostsSubscriber.unsubscribe();
    }

    @SuppressWarnings("unchecked") @Override public void onNext(DataSnapshot dataSnapshot) {
      List<BlogPostEntity> blogPostEntities = new ArrayList<>();
      for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
        String keys = childDataSnapshot.getKey();
        Log.d("keys ", keys);
        BlogPostEntity resultx = childDataSnapshot.getValue(BlogPostEntity.class);
        resultx.setAuthor(keys);
        blogPostEntities.add(resultx);
      }
      if( kolko >= 0 ) { renderBlogPosts(blogPostEntities); }
      kolko=kolko+1;
    }
  }


  public static class TapEvent {}

  private void delBlogPostRx(BlogPostEntity postx, int del, String keys) {


      final DatabaseReference firebaseRefdel = FirebaseDatabase.getInstance().getReference().child("fireblog").child(keys);
      RxFirebaseDatabase.getInstance().observeDelValuePush(firebaseRefdel, postx, del).subscribe(setPostsSubscriber);



  }//end of add BlogPostEntity

  /**
   * Subscriber for {@link //RxFirebaseDatabase} query
   */
  private final class SetPostsSubscriber extends Subscriber<String> {
    @Override public void onCompleted() {
      showProgress(false);
      setPostsSubscriber.unsubscribe();
    }

    @Override public void onError(Throwable e) {
      showProgress(false);
      showError(e.getMessage());
      setPostsSubscriber.unsubscribe();
    }

    @SuppressWarnings("unchecked") @Override public void onNext(String keyf) {
      showMessage(keyf);
    }
  }//end of setpostssubscriber

  private void showMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
  }

  private void addBlogPostRx(BlogPostEntity postx, int del) {

    final DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference().child("fireblog");
    RxFirebaseDatabase.getInstance().observeSetValuePush(firebaseRef, postx, del).subscribe(setPostsSubscriber);


  }//end of add BlogPostEntity

}
