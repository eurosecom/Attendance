package com.eusecom.attendance.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttendanceViewHolder extends RecyclerView.ViewHolder  {

    public TextView absence_name;
    public ImageView starView;
    public TextView numStarsView;
    public TextView datefrom;
    public TextView dateto;
    public TextView hodxb;
    public TextView datm;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    String usemail = "";

    public AttendanceViewHolder(View itemView) {
        super(itemView);

        absence_name = (TextView) itemView.findViewById(R.id.absence_name);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        datefrom = (TextView) itemView.findViewById(R.id.datefrom);
        dateto = (TextView) itemView.findViewById(R.id.dateto);
        hodxb = (TextView) itemView.findViewById(R.id.hodxb);
        datm = (TextView) itemView.findViewById(R.id.datm);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            usemail = user.getEmail();
        }
    }

    public void bindToAttendance(com.eusecom.attendance.models.Attendance attendance, View.OnClickListener starClickListener) {
        absence_name.setText(attendance.dmxa + " " + attendance.dmna);
        numStarsView.setText("0");

        //convert unix epoch timestamp (seconds) to milliseconds
        long timestampod = Long.parseLong(attendance.daod) * 1000L;
        datefrom.setText(getDate(timestampod ));

        long timestampdo = Long.parseLong(attendance.dado) * 1000L;
        dateto.setText(getDate(timestampdo ));

        long timestamp = Long.parseLong(attendance.daod) * 1000L;
        hodxb.setText(attendance.hodxb);

        long timestampm = Long.parseLong(attendance.datm) * 1000L;
        datm.setText(usemail + " " + getDateTime(timestampm ));

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
            DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }


}
