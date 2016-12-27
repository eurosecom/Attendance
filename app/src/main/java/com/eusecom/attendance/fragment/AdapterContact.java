/*
 * http://stackoverflow.com/questions/41224253/firebase-database-with-recycler-view-in-fragment
 */

package com.eusecom.attendance.fragment;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eusecom.attendance.R;
import com.eusecom.attendance.models.Contact;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.MyViewHolder> {
Context context;
ArrayList<Contact> contacts;



public static class MyViewHolder extends RecyclerView.ViewHolder {
    public CardView mCardView1;
    public TextView mTextView1,mTextView2,mTextView3,mTextView4;
    public MyViewHolder(View v) {
        super(v);
        //mCardView1 = (CardView) v.findViewById(R.layout.item_contact);
        mTextView1 = (TextView) v.findViewById(R.id.contact_profession);
        mTextView2 = (TextView) v.findViewById(R.id.contact_name);
        //mTextView3 = (TextView) v.findViewById(R.id.contact_address);
        //mTextView4 = (TextView) v.findViewById(R.id.contact_number);
    }
}
public AdapterContact(Context context,ArrayList<Contact> contacts) {
    this.contacts = contacts;
    this.context = context;

}
@Override
public AdapterContact.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_contact, parent, false);
    // set the view's size, margins, paddings and layout parameters
    return new MyViewHolder(v);
}

@Override
public void onBindViewHolder(MyViewHolder holder, int position) {

    holder.mTextView1.setText(contacts.get(position).getContactProfession());
    holder.mTextView2.setText(contacts.get(position).getContactName());
    //holder.mTextView3.setText(contacts.get(position).getContactAddress());
    //holder.mTextView4.setText(contacts.get(position).getContactNumber());
    int x = contacts.get(position).getContactStatus();

}
@Override
public int getItemCount() {
    return this.contacts.size();
}
}