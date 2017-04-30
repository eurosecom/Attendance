package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import butterknife.ButterKnife;

import static android.view.View.inflate;

/**
 * The view holder for an item
 */
public class BlogPostViewHolder extends RecyclerView.ViewHolder {

  public TextView postsTitle;
  public TextView postsAuthor;


  public BlogPostViewHolder(ViewGroup parent) {
    super(inflate(parent.getContext(), R.layout.row_rxfirebase_post, null));
    ButterKnife.bind(this, itemView);
    postsTitle = (TextView) itemView.findViewById(R.id.postsTitle);
    postsAuthor = (TextView) itemView.findViewById(R.id.postsAuthor);
  }

  public void bindModel(BlogPostEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    this.postsTitle.setText(entity.getTitle());
    this.postsAuthor.setText(entity.getAuthor());
  }
}
