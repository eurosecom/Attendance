/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eusecom.attendance;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eusecom.attendance.models.Attendance;
import com.eusecom.attendance.models.EventRxBus;
import com.eusecom.attendance.retrofit.RfEtestApi;
import com.eusecom.attendance.rxbus.RxBus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import static com.eusecom.attendance.R.id.datefrom;
import static com.eusecom.attendance.R.id.dateto;

public class AbsServerAsAdapter extends RecyclerView.Adapter<AbsServerAsAdapter.AbsServerAsViewHolder> {

    private List<String> mCheeses;
    private List<Attendance> mListabsserver;
    private RxBus _rxBus;

    AbsServerAsAdapter(RxBus bus){

        _rxBus = bus;
    }

  @Override
  public AbsServerAsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absserver,parent,false);

    return new AbsServerAsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AbsServerAsViewHolder holder, int position) {

      holder.absence_name.setText(mListabsserver.get(position).dmxa + " " + mListabsserver.get(position).dmna);
      if( mListabsserver.get(position).dmxa.equals("1")) {
          Picasso.with(holder.mContext).load(R.drawable.abs520).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("2")) {
          Picasso.with(holder.mContext).load(R.drawable.abs510).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("506")) {
          Picasso.with(holder.mContext).load(R.drawable.abs506).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("510")) {
          Picasso.with(holder.mContext).load(R.drawable.abs510).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("518")) {
          Picasso.with(holder.mContext).load(R.drawable.abs518).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("520")) {
          Picasso.with(holder.mContext).load(R.drawable.abs520).resize(120, 120).into(holder.absence_photo);
      }
      if( mListabsserver.get(position).dmxa.equals("801")) {
          Picasso.with(holder.mContext).load(R.drawable.abs801).resize(120, 120).into(holder.absence_photo);
      }

      holder.numStarsView.setText(mListabsserver.get(position).aprv);
      if( mListabsserver.get(position).aprv.equals("1")) {
          Picasso.with(holder.mContext).load(R.drawable.ic_check_circle_black_24dp).resize(120, 120).into(holder.starView);
      }
      if( mListabsserver.get(position).aprv.equals("2")) {
          Picasso.with(holder.mContext).load(R.drawable.ic_remove_circle_black_24dp).resize(120, 120).into(holder.starView);
      }

      holder.datefrom.setText(mListabsserver.get(position).daod);

      holder.dateto.setText(mListabsserver.get(position).dado);

      holder.hodxb.setText(mListabsserver.get(position).hodxb);

      holder.datm.setText(mListabsserver.get(position).lati + " " + mListabsserver.get(position).datm);



      holder.setClickListener(new AbsServerAsAdapter.AbsServerAsViewHolder.ClickListener() {
          public void onClick(View v, int pos, boolean isLongClick) {
              if (isLongClick) {

                  // View v at position pos is long-clicked.
                  Log.d("onLongClickListener", mListabsserver.get(pos).dmna);
                  getDialog(mListabsserver.get(position).longi, mListabsserver.get(position), holder.mContext);

              } else {

                  Log.d("onShortClickListener", mListabsserver.get(pos).dmna);
              }
          }
      });

  }//end onbindviewholder

    @Override
    public int getItemCount() {
        return mListabsserver == null ? 0 : mListabsserver.size();
    }


    public void setAbsserver(List<Attendance> listabsserver) {
        mListabsserver = listabsserver;
        //Log.d("setAbsserver ", mListabsserver.get(0).dmna);
        notifyDataSetChanged();
    }


  public static class AbsServerAsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      public TextView absence_name;
      public ImageView absence_photo;
      public ImageView starView;
      public TextView numStarsView;
      public TextView datefrom;
      public TextView dateto;
      public TextView hodxb;
      public TextView datm;
      private ClickListener clickListener;
      Context mContext;
      FirebaseUser user;
      private FirebaseAuth mAuth;
      String usemail = "";

    public AbsServerAsViewHolder(View itemView) {
        super(itemView);

        absence_name = (TextView) itemView.findViewById(R.id.absence_name);
        absence_photo = (ImageView) itemView.findViewById(R.id.absence_photo);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        datefrom = (TextView) itemView.findViewById(R.id.datefrom);
        dateto = (TextView) itemView.findViewById(R.id.dateto);
        hodxb = (TextView) itemView.findViewById(R.id.hodxb);
        datm = (TextView) itemView.findViewById(R.id.datm);
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            usemail = user.getEmail();
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


  }//end class viewholder

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


    private void getDialog(String postkey, Attendance model, Context mContext) {

        String fromfir =  SettingsActivity.getFir(mContext);
        int savetofiri = Integer.parseInt(fromfir);

        // custom dialog
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.absserver_dialog);
        dialog.setTitle(R.string.item);
        // set the custom dialog components - text, image and button
        String textx = mContext.getString(R.string.item) + " " + postkey + ". " + model.lati + ". " + model.dmna + " " + model.daod + " / " + model.dado;
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(textx);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_edit);

        Button buttonDownload = (Button) dialog.findViewById(R.id.buttonDownload);
        // if button is clicked, close the custom dialog
        buttonDownload.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (_rxBus.hasObservers()) {
                    ///_rxBus.send(new EventRxBus.Message(model.daod)); OK
                    //_rxBus.send(new EventRxBus.Absence(model.dmxa, model.dmna, model.daod, model.dado, model.dnixa, model.hodxb)); OK
                    _rxBus.send(model);
                    _rxBus.send(new AbsServerAsBaseSearchActivity.TapEvent());
                }
                dialog.dismiss();

            }
        });
        Button buttonClose = (Button) dialog.findViewById(R.id.buttonClose);
        // if button is clicked, close the custom dialog
        buttonClose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.show();

    }//end getdialog


}//end class adapter
