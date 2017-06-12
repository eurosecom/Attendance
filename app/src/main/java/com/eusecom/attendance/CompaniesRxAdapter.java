package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class CompaniesRxAdapter extends RecyclerView.Adapter<CompaniesRxViewHolder> {

  private List<Company> mBlogPostEntities;
  private RxBus _rxBus;

  public CompaniesRxAdapter(List<Company> blogPostEntities, RxBus bus) {
    mBlogPostEntities = blogPostEntities;
    _rxBus = bus;
  }

  @Override public CompaniesRxViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_item, viewGroup, false);
    return new CompaniesRxViewHolder(view);
  }

  @Override public void onBindViewHolder(CompaniesRxViewHolder holder, int position) {
    Company blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity);

    holder.setClickListener(new CompaniesRxViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String icos = blogPostEntity.getCmico();
        if (isLongClick) {

          Log.d("longClick", pos + " " + icos);
          //remove(position);
          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
          }

        } else {

          //Log.d("shortClick", pos + " " + keys);

        }
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



  public void add(Company addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Employee}
   */
  public void setData(List<Company> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  public Company getItemAtPosition(int position) {
    return mBlogPostEntities.get(position);
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Employee}
   */
  public void validateData(List<Company> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
