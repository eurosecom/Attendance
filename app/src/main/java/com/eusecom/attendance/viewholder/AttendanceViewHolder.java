package com.eusecom.attendance.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.R;

public class AttendanceViewHolder extends RecyclerView.ViewHolder  {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;

    public AttendanceViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);



    }

    public void bindToAttendance(com.eusecom.attendance.models.Attendance attendance, View.OnClickListener starClickListener) {
        titleView.setText(attendance.dmxa);
        authorView.setText(attendance.dmna);
        numStarsView.setText("0");
        bodyView.setText("0");

        starView.setOnClickListener(starClickListener);


    }




}
