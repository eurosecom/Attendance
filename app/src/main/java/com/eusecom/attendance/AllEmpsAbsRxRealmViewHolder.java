package com.eusecom.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.eusecom.attendance.realm.RealmEmployee;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;


/**
 * The view holder for an item
 */
public class AllEmpsAbsRxRealmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

  public TextView employee_name;
  public ImageView employee_photo;
  public ImageView starView;
  public TextView numStarsView;
  public TextView oscx;
  public TextView icox;
  public TextView typx;
  public TextView emailx;
  public TextView day01;
  public TextView day02;
  public TextView day03;
  public TextView day04;
  public TextView day05;
  public TextView day06;
  public TextView day07;
  public TextView day08;
  public TextView day09;
  public TextView day10;

  public TextView day11;
  public TextView day12;
  public TextView day13;
  public TextView day14;
  public TextView day15;
  public TextView day16;
  public TextView day17;
  public TextView day18;
  public TextView day19;
  public TextView day20;

  public TextView day21;
  public TextView day22;
  public TextView day23;
  public TextView day24;
  public TextView day25;
  public TextView day26;
  public TextView day27;
  public TextView day28;
  public TextView day29;
  public TextView day30;
  public TextView day31;

  private ClickListener clickListener;
  Context mContext;


  public AllEmpsAbsRxRealmViewHolder(View itemView) {
    super(itemView);

    employee_name = (TextView) itemView.findViewById(R.id.employee_name);
    employee_photo = (ImageView) itemView.findViewById(R.id.employee_photo);
    starView = (ImageView) itemView.findViewById(R.id.star);
    numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
    oscx = (TextView) itemView.findViewById(R.id.oscx);
    icox = (TextView) itemView.findViewById(R.id.icox);
    typx = (TextView) itemView.findViewById(R.id.typx);
    emailx = (TextView) itemView.findViewById(R.id.emailx);
    mContext = itemView.getContext();

    day01 = (TextView) itemView.findViewById(R.id.day01);
    day02 = (TextView) itemView.findViewById(R.id.day02);
    day03 = (TextView) itemView.findViewById(R.id.day03);
    day04 = (TextView) itemView.findViewById(R.id.day04);
    day05 = (TextView) itemView.findViewById(R.id.day05);
    day06 = (TextView) itemView.findViewById(R.id.day06);
    day07 = (TextView) itemView.findViewById(R.id.day07);
    day08 = (TextView) itemView.findViewById(R.id.day08);
    day09 = (TextView) itemView.findViewById(R.id.day09);
    day10 = (TextView) itemView.findViewById(R.id.day10);

    day11 = (TextView) itemView.findViewById(R.id.day11);
    day12 = (TextView) itemView.findViewById(R.id.day12);
    day13 = (TextView) itemView.findViewById(R.id.day13);
    day14 = (TextView) itemView.findViewById(R.id.day14);
    day15 = (TextView) itemView.findViewById(R.id.day15);
    day16 = (TextView) itemView.findViewById(R.id.day16);
    day17 = (TextView) itemView.findViewById(R.id.day17);
    day18 = (TextView) itemView.findViewById(R.id.day18);
    day19 = (TextView) itemView.findViewById(R.id.day19);
    day20 = (TextView) itemView.findViewById(R.id.day20);

    day21 = (TextView) itemView.findViewById(R.id.day21);
    day22 = (TextView) itemView.findViewById(R.id.day22);
    day23 = (TextView) itemView.findViewById(R.id.day23);
    day24 = (TextView) itemView.findViewById(R.id.day24);
    day25 = (TextView) itemView.findViewById(R.id.day25);
    day26 = (TextView) itemView.findViewById(R.id.day26);
    day27 = (TextView) itemView.findViewById(R.id.day27);
    day28 = (TextView) itemView.findViewById(R.id.day28);
    day29 = (TextView) itemView.findViewById(R.id.day29);
    day30 = (TextView) itemView.findViewById(R.id.day30);
    day31 = (TextView) itemView.findViewById(R.id.day31);

    itemView.setOnClickListener(this);
    itemView.setOnLongClickListener(this);
  }

  public void bindModel(RealmEmployee employee, String ume) {
    if (employee == null) {
      throw new IllegalArgumentException("Entity cannot be null");
    }
    employee_name.setText(employee.getUsername());
    oscx.setText(employee.getUsosc());
    icox.setText(employee.getUsico());
    typx.setText(employee.getUstype());
    emailx.setText(employee.getEmail());

    StringTokenizer tokens = new StringTokenizer(ume, ".");
    String mesiac = tokens.nextToken();
    String rok = tokens.nextToken();

    for(int l=1; l<=31; l++){

      String sdate = rok + "-" + mesiac + "-" + l;
      int nday = getDaysNumber(sdate);
      String sday = nday + "";
      Log.d("sdate sday  ", sdate + " " + sday);

      if( nday == 1 && l == 1 ) { day01.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 2 ) { day02.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 3 ) { day03.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 4 ) { day04.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 5 ) { day05.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 6 ) { day06.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 7 ) { day07.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 8 ) { day08.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 9 ) { day09.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 10 ) { day10.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }

      if( nday == 1 && l == 11 ) { day11.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 12 ) { day12.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 13 ) { day13.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 14 ) { day14.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 15 ) { day15.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 16 ) { day16.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 17 ) { day17.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 18 ) { day18.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 19 ) { day19.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 20 ) { day20.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }

      if( nday == 1 && l == 21 ) { day21.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 22 ) { day22.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 23 ) { day23.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 24 ) { day24.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 25 ) { day25.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 26 ) { day26.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 27 ) { day27.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 28 ) { day28.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 29 ) { day29.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 30 ) { day30.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }
      if( nday == 1 && l == 31 ) { day31.setBackgroundColor(mContext.getResources().getColor(R.color.material_lime_A200)); }



    }

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
