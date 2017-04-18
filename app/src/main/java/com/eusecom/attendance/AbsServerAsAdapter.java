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

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eusecom.attendance.models.Attendance;

import java.util.List;

public class AbsServerAsAdapter extends RecyclerView.Adapter<AbsServerAsAdapter.AbsServerAsViewHolder> {

    private List<String> mCheeses;
    private List<Attendance> mListabsserver;

  @Override
  public AbsServerAsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    //View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absserver,parent,false);
    return new AbsServerAsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AbsServerAsViewHolder holder, int position) {
      //holder.title.setText(mCheeses.get(position));
      holder.title.setText(mListabsserver.get(position).dmna);

      holder.setClickListener(new AbsServerAsAdapter.AbsServerAsViewHolder.ClickListener() {
          public void onClick(View v, int pos, boolean isLongClick) {
              if (isLongClick) {

                  // View v at position pos is long-clicked.
                  Log.d("onLongClickListener", mListabsserver.get(pos).dmna);

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
        Log.d("setAbsserver ", mListabsserver.get(0).dmna);
        notifyDataSetChanged();
    }


  public static class AbsServerAsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public final TextView title;
      private ClickListener clickListener;

    public AbsServerAsViewHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.datefrom);
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

}//end class adapter
