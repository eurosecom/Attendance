package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;

/**
 * The view holder for an item
 */
public class BlogPostViewHolder extends RecyclerView.ViewHolder {

  public TextView postsTitle;
  public TextView postsAuthor;


  public BlogPostViewHolder(View itemView) {
    super(itemView);
    //postsTitle = (TextView) itemView.findViewById(R.id.postsTitle);
    //postsAuthor = (TextView) itemView.findViewById(R.id.postsAuthor);
    postsTitle = (TextView) itemView.findViewById(R.id.absence_name);
    postsAuthor = (TextView) itemView.findViewById(R.id.datefrom);
  }

  public void bindModel(BlogPostEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    postsTitle.setText(entity.getTitle());
    postsAuthor.setText(entity.getAuthor());
  }
}
