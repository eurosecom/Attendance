package com.sqisland.android.test_demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sqisland.android.test_demo.realm.RealmEmployee;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;


/**
 * The view holder for an item
 */
public class AllEmpsAbsRxRealmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

  public TextView employee_name;
  public ImageView employee_photo;


  private ClickListener clickListener;
  Context mContext;


  public AllEmpsAbsRxRealmViewHolder(View itemView) {
    super(itemView);

    employee_name = (TextView) itemView.findViewById(R.id.employee_name);
    employee_photo = (ImageView) itemView.findViewById(R.id.employee_photo);

    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  public void bindModel(RealmEmployee employee, String ume) {
    if (employee == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    employee_name.setText(employee.getUsername());


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


  private int getDaysNumber(String daydate){

    try{
      SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date date = inFormat.parse(daydate);
      Calendar c = Calendar.getInstance();
      c.setTime(date); // yourdate is an object of type Date
      int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
      return dayOfWeek;

    }
    catch(Exception ex){
      return 0;
    }
  }

}
