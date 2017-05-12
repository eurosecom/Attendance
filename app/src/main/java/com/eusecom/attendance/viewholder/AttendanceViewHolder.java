package com.eusecom.attendance.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceViewHolder extends RecyclerView.ViewHolder  {

    public TextView absence_name;
    public ImageView absence_photo;
    public ImageView starView;
    public TextView numStarsView;
    public TextView datm;
    Context mContext;

    public AttendanceViewHolder(View itemView) {
        super(itemView);

        absence_name = (TextView) itemView.findViewById(R.id.absence_name);
        absence_photo = (ImageView) itemView.findViewById(R.id.absence_photo);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        datm = (TextView) itemView.findViewById(R.id.datm);
        mContext = itemView.getContext();

    }

    public void bindToAttendance(com.eusecom.attendance.models.Attendance attendance, View.OnClickListener starClickListener) {
        absence_name.setText(attendance.dmxa + " " + attendance.dmna);
        if( attendance.dmxa.equals("1")) {
            Picasso.with(mContext).load(R.drawable.intowork).resize(120, 120).into(absence_photo);
        }
        if( attendance.dmxa.equals("2")) {
            Picasso.with(mContext).load(R.drawable.outsidework).resize(120, 120).into(absence_photo);
        }
        numStarsView.setText("0");

        //long timestampm = Long.parseLong(attendance.datm) * 1000L;
        long timestampm = attendance.getDatsLong();
        datm.setText(attendance.usname + " " + getDateTime(timestampm ));

        starView.setOnClickListener(starClickListener);

    }

    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }

    private String getDateTime(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


}
