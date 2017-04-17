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

public class AbsServerAdapter extends RecyclerView.Adapter<AbsServerAdapter.AbsServerViewHolder> {

    private List<String> mCheeses;
    private List<Attendance> mListabsserver;

  @Override
  public AbsServerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    //View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);

    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absserver,parent,false);
    return new AbsServerViewHolder(view);
  }

  @Override
  public void onBindViewHolder(AbsServerViewHolder holder, int position) {
      //holder.title.setText(mCheeses.get(position));
      holder.title.setText(mListabsserver.get(position).dmna);
  }

    @Override
    //public int getItemCount() { return mCheeses == null ? 0 : mCheeses.size();}
    public int getItemCount() {
        return mListabsserver == null ? 0 : mListabsserver.size();
    }

    public void setCheeses(List<String> cheeses) {
        mCheeses = cheeses;
        notifyDataSetChanged();
    }

    public void setAbsserver(List<Attendance> listabsserver) {
        mListabsserver = listabsserver;
        Log.d("setAbsserver ", mListabsserver.get(0).dmna);
        notifyDataSetChanged();
    }


  public static class AbsServerViewHolder extends RecyclerView.ViewHolder {
    public final TextView title;

    public AbsServerViewHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.datefrom);
    }
  }
}
