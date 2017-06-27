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
import com.eusecom.attendance.models.Company;
import com.eusecom.attendance.rxbus.RxBus;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CompanyChooseAdapter extends RecyclerView.Adapter<CompanyChooseAdapter.CompanyChooseViewHolder> {

    private List<Company> mListcompany;
    private RxBus _rxBus;

    CompanyChooseAdapter(RxBus bus){

        _rxBus = bus;
    }

  @Override
  public CompanyChooseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item,parent,false);

    return new CompanyChooseViewHolder(view);
  }

  @Override
  public void onBindViewHolder(CompanyChooseViewHolder holder, int position) {

      holder.employee_name.setText(mListcompany.get(position).cmname);
      holder.icox.setText(mListcompany.get(position).cmico);

      holder.setClickListener(new CompanyChooseAdapter.CompanyChooseViewHolder.ClickListener() {
          public void onClick(View v, int pos, boolean isLongClick) {
              if (isLongClick) {

                  // View v at position pos is long-clicked.
                  Log.d("onLongClickListener", mListcompany.get(pos).cmname);
                  getDialog(mListcompany.get(position).cmico, mListcompany.get(position), holder.mContext);

              } else {

                  Log.d("onShortClickListener", mListcompany.get(pos).cmname);
              }
          }
      });

  }//end onbindviewholder

    @Override
    public int getItemCount() {
        return mListcompany == null ? 0 : mListcompany.size();
    }


    public void setCompany(List<Company> listcompany) {
        mListcompany = listcompany;
        notifyDataSetChanged();
    }


  public static class CompanyChooseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

      public TextView employee_name;
      public ImageView employee_photo;
      public ImageView starView;
      public TextView numStarsView;
      public TextView oscx;
      public TextView icox;
      public TextView typx;
      public TextView emailx;
      private ClickListener clickListener;
      Context mContext;

    public CompanyChooseViewHolder(View itemView) {
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
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);


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


    private void getDialog(String postkey, Company model, Context mContext) {

        String fromfir =  SettingsActivity.getFir(mContext);
        int savetofiri = Integer.parseInt(fromfir);

        // custom dialog
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.absserver_dialog);
        dialog.setTitle(R.string.item);
        // set the custom dialog components - text, image and button
        String textx = mContext.getString(R.string.item) + " " + postkey + ". " + model.cmname;
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
                    _rxBus.send(new CompanyChooseBaseSearchActivity.OnItemClickEvent());
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
