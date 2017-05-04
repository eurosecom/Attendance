package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class BlogPostsAdapter extends RecyclerView.Adapter<BlogPostViewHolder> {

  private List<BlogPostEntity> mBlogPostEntities;

  public BlogPostsAdapter(List<BlogPostEntity> blogPostEntities) {
    mBlogPostEntities = blogPostEntities;
  }

  @Override public BlogPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    //View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rxfirebase_post, viewGroup, false);
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_approve, viewGroup, false);
    return new BlogPostViewHolder(view);
  }

  @Override public void onBindViewHolder(BlogPostViewHolder holder, int position) {
    BlogPostEntity blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity);

    holder.setClickListener(new BlogPostViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {
        if (isLongClick) {

          Log.d("longClick", pos + "");


        } else {

          Log.d("shortClick", pos + "");

        }
      }
    });

  }

  @Override public int getItemCount() {
    Log.d("getItemCount", mBlogPostEntities.size() + "");
    return mBlogPostEntities.size();

  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link BlogPostEntity}
   */
  public void setData(List<BlogPostEntity> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link BlogPostEntity}
   */
  public void validateData(List<BlogPostEntity> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
