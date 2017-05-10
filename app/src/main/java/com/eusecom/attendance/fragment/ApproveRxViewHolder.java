package com.eusecom.attendance.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eusecom.attendance.R;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;

/**
 * The view holder for an item
 */
public class ApproveRxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

  public TextView postsTitle;
  public TextView postsAuthor;
  private ClickListener clickListener;


  public ApproveRxViewHolder(View itemView) {
    super(itemView);
    //postsTitle = (TextView) itemView.findViewById(R.id.postsTitle);
    //postsAuthor = (TextView) itemView.findViewById(R.id.postsAuthor);
    postsTitle = (TextView) itemView.findViewById(R.id.absence_name);
    postsAuthor = (TextView) itemView.findViewById(R.id.datefrom);

    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  public void bindModel(BlogPostEntity entity) {
    if (entity == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    postsTitle.setText(entity.getTitle());
    postsAuthor.setText(entity.getAuthor());
  }

  /* Interface for handling clicks - both normal and long ones. */
  public interface ClickListener {

    /**
     * Called when the view is clicked.
     *
     * @param v view that is clicked
     * @param position of the clicked item
     * @param isLongClick true if long click, false otherwise
     */
    public void onClick(View v, int position, boolean isLongClick);

  }

  /* Setter for listener. */
  public void setClickListener(ClickListener clickListener) {
    this.clickListener = clickListener;
  }

  @Override
  public void onClick(View v) {

    // If not long clicked, pass last variable as false.
    clickListener.onClick(v, getPosition(), false);
  }

  @Override
  public boolean onLongClick(View v) {

    // If long clicked, passed last variable as true.
    clickListener.onClick(v, getPosition(), true);
    return true;
  }



}
