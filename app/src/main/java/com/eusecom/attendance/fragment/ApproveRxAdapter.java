package com.eusecom.attendance.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.attendance.R;
import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;

import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class ApproveRxAdapter extends RecyclerView.Adapter<ApproveRxViewHolder> {

  private List<Attendance> mBlogPostEntities;
  private RxBus _rxBus;

  public ApproveRxAdapter(List<Attendance> blogPostEntities, RxBus bus) {
    mBlogPostEntities = blogPostEntities;
    _rxBus = bus;
  }

  @Override public ApproveRxViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_approve, viewGroup, false);
    return new ApproveRxViewHolder(view);
  }

  @Override public void onBindViewHolder(ApproveRxViewHolder holder, int position) {
    Attendance blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity);

    holder.setClickListener(new ApproveRxViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = blogPostEntity.getRok();
        if (isLongClick) {

          Log.d("longClick", pos + " " + keys);
          //remove(position);
          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
            _rxBus.send(new ApproveListFragment.TapEvent());
          }

        } else {

          Log.d("shortClick", pos + " " + keys);

        }
      }
    });

  }

  @Override public int getItemCount() {
    Log.d("getItemCount", mBlogPostEntities.size() + "");
    return mBlogPostEntities.size();
  }


  public void remove(int position) {
    mBlogPostEntities.remove(position);
    notifyItemRemoved(position);
  }



  public void add(Attendance addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Attendance}
   */
  public void setData(List<Attendance> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Attendance}
   */
  public void validateData(List<Attendance> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
