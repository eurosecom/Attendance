package com.eusecom.attendance.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.attendance.R;
import com.eusecom.attendance.models.Abstype;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;

import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class AbsTypesRxAdapter extends RecyclerView.Adapter<AbsTypesRxViewHolder> {

  private List<Abstype> mBlogPostEntities;
  private RxBus _rxBus;

  public AbsTypesRxAdapter(List<Abstype> blogPostEntities, RxBus bus) {
    mBlogPostEntities = blogPostEntities;
    _rxBus = bus;
  }

  @Override public AbsTypesRxViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_abstypes, viewGroup, false);
    return new AbsTypesRxViewHolder(view);
  }

  @Override public void onBindViewHolder(AbsTypesRxViewHolder holder, int position) {
    Abstype blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity);

    holder.setClickListener(new AbsTypesRxViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = "xxxxxxxx";
        if (isLongClick) {

          Log.d("longClick", pos + " " + keys);
          //remove(position);
          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
            _rxBus.send(new AbsTypesListRxFragment.TapEvent());
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



  public void add(Abstype addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Abstype}
   */
  public void setData(List<Abstype> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Abstype}
   */
  public void validateData(List<Abstype> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
