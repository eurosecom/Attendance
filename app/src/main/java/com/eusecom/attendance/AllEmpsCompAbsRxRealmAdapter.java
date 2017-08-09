package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.realm.RealmCompany;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class AllEmpsCompAbsRxRealmAdapter extends RecyclerView.Adapter<AllEmpsCompAbsRxRealmViewHolder> {

  private List<RealmCompany> mBlogPostEntities;
  private String umex;
  private RxBus _rxBus;

  public AllEmpsCompAbsRxRealmAdapter(List<RealmCompany> blogPostEntities, RxBus bus, String ume) {
    mBlogPostEntities = blogPostEntities;
    umex = ume;
    _rxBus = bus;
    //do not work _rxBus = AttendanceApplication.getInstance().getRxBusSingleton();
  }

  @Override public AllEmpsCompAbsRxRealmViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.allempsabs_item, viewGroup, false);
    return new AllEmpsCompAbsRxRealmViewHolder(view);
  }

  @Override public void onBindViewHolder(AllEmpsCompAbsRxRealmViewHolder holder, int position) {
    RealmCompany blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity, umex);

    holder.setClickListener(new AllEmpsCompAbsRxRealmViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = blogPostEntity.getKeyf();
        if (isLongClick) {

          //Log.d("longClick", pos + " " + keys);
          //remove(position);
          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
            //_rxBus.send(new EmployeeMvvmActivity.TapEvent());
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



  public void add(RealmCompany addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Employee}
   */
  public void setRealmData(List<RealmCompany> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  public RealmCompany getItemAtPosition(int position) {
    return mBlogPostEntities.get(position);
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Employee}
   */
  public void validateData(List<RealmCompany> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
