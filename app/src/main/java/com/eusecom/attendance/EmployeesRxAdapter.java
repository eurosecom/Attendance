package com.eusecom.attendance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eusecom.attendance.models.Employee;
import com.eusecom.attendance.rxbus.RxBus;
import com.eusecom.attendance.rxfirebase2models.BlogPostEntity;
import java.util.List;

/**
 * The {@link RecyclerView.Adapter} with a {@link List} of {@link BlogPostEntity}
 */
class EmployeesRxAdapter extends RecyclerView.Adapter<EmployeesRxViewHolder> {

  private List<Employee> mBlogPostEntities;
  private RxBus _rxBus;

  public EmployeesRxAdapter(List<Employee> blogPostEntities, RxBus bus) {
    mBlogPostEntities = blogPostEntities;
    _rxBus = bus;
    //_rxBus = AttendanceApplication.getInstance().getRxBusSingleton();
  }

  @Override public EmployeesRxViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_item, viewGroup, false);
    return new EmployeesRxViewHolder(view);
  }

  @Override public void onBindViewHolder(EmployeesRxViewHolder holder, int position) {
    Employee blogPostEntity = mBlogPostEntities.get(position);
    holder.bindModel(blogPostEntity);

    holder.setClickListener(new EmployeesRxViewHolder.ClickListener() {
      public void onClick(View v, int pos, boolean isLongClick) {

        String keys = blogPostEntity.getKeyf();
        if (isLongClick) {

          //Log.d("longClick", pos + " " + keys);
          //remove(position);
          if (_rxBus.hasObservers()) {
            _rxBus.send(blogPostEntity);
            _rxBus.send(new EmployeeMvvmActivity.TapEvent());
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



  public void add(Employee addmodel, int position) {
    mBlogPostEntities.add(position, addmodel);
    notifyItemInserted(position);
  }

  /**
   * Sets the data for adapter
   *
   * @param blogPost a {@link List} of {@link Employee}
   */
  public void setData(List<Employee> blogPost) {
    this.validateData(blogPost);
    mBlogPostEntities = blogPost;
    //Log.d("AdapterSetData", mBlogPostEntities.get(0).getTitle());
    //Log.d("AdapterSetData", mBlogPostEntities.get(1).getTitle());
    this.notifyDataSetChanged();
  }

  public Employee getItemAtPosition(int position) {
    return mBlogPostEntities.get(position);
  }

  /**
   * Validates the data
   *
   * @param blogPostEntities a {@link List} of {@link Employee}
   */
  public void validateData(List<Employee> blogPostEntities) {
    if (blogPostEntities == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }
}
