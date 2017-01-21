package com.eusecom.attendance.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbsTypesViewHolder extends RecyclerView.ViewHolder  {

    public TextView absence_name;
    public ImageView absence_photo;
    public ImageView starView;
    public TextView numStarsView;
    Context mContext;


    public AbsTypesViewHolder(View itemView) {
        super(itemView);

        absence_name = (TextView) itemView.findViewById(R.id.absence_name);
        absence_photo = (ImageView) itemView.findViewById(R.id.absence_photo);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        mContext = itemView.getContext();

    }

    public void bindToAbsence(com.eusecom.attendance.models.Abstype abstypes, View.OnClickListener starClickListener) {

        long timestampm = abstypes.getDatmLong();

        absence_name.setText(abstypes.idm + " " + abstypes.iname + " " + getDateTime(timestampm ));
        if( abstypes.idm.equals("506")) {
            Picasso.with(mContext).load(R.drawable.abs506).resize(120, 120).into(absence_photo);
        }
        if( abstypes.idm.equals("510")) {
            Picasso.with(mContext).load(R.drawable.abs510).resize(120, 120).into(absence_photo);
        }
        if( abstypes.idm.equals("518")) {
            Picasso.with(mContext).load(R.drawable.abs518).resize(120, 120).into(absence_photo);
        }
        if( abstypes.idm.equals("520")) {
            Picasso.with(mContext).load(R.drawable.abs520).resize(120, 120).into(absence_photo);
        }
        if( abstypes.idm.equals("801")) {
            Picasso.with(mContext).load(R.drawable.abs801).resize(120, 120).into(absence_photo);
        }
        numStarsView.setText("0");


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
