package com.sqisland.android.test_demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sqisland.android.test_demo.realm.RealmEmployee;

import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link --BlogPostEntity}
 */
class AllEmpsAbsRxRealmAdapter extends RecyclerView.Adapter<AllEmpsAbsRxRealmViewHolder> {

  private List<RealmEmployee> mBlogPostEntities;
  private String umex;

  public AllEmpsAbsRxRealmAdapter(List<RealmEmployee> blogPostEntities, String ume) {
    mBlogPostEntities = blogPostEntities;
    umex = ume;
    //do not work _rxBus = AttendanceApplication.getInstance().getRxBusSingleton();
  }

  @Override public AllEmpsAbsRxRealmViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allempsabs_item, viewGroup, false);
    return new AllEmpsAbsRxRealmViewHolder(view);
  }

  @Override public void onBindViewHolder(AllEmpsAbsRxRealmViewHolder holder, int position) {
    RealmEmployee blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity, umex);

    holder.setClickListener(new AllEmpsAbsRxRealmViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = blogPostEntity.getKeyf();

      }
    });

  }

  @Override public int getItemCount() {
    //Log.d("getItemCount", mBlogPostEntities.size() + "");
    return mBlogPostEntities.size();
  }


  public void remove(int position) {
    mBlogPostEntities.remove(position);
    notifyItemRemoved(position);
  }



  public void add(RealmEmployee addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link --Employee}
   */
  public void setRealmData(List<RealmEmployee> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  public RealmEmployee getItemAtPosition(int position) {
    return mBlogPostEntities.get(position);
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link --Employee}
   */
  public void validateData(List<RealmEmployee> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
