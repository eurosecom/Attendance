package com.eusecom.attendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
     
    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
                                               // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
 
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
     
    private String name;        //String Resource for header View Name
    private int profile;        //int Resource for header view profile picture
    private String email;       //String Resource for header view email
    private Context mContext;

    public MyAdapter(Context context, String Titles[],int Icons[],String Name,String Email, int Profile){ // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        email = Email;
        profile = Profile;
        mContext = context;
        //here we assign those passed values to the values we declared here
        //in adapter

    }
 
 
    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them
 
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        int Holderid;
        TextView textView; 
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;
        private ClickListener clickListener;


 
        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
             
            
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created
             
            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
            else{
 
 
                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
                // We set listeners to the whole item view, but we could also
                // specify listeners for the title or the icon.

            }


        }//end constructor ViewHolder

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
 
         
    }//end class ViewHolder
 

    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder
 
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
 
        if (viewType == TYPE_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout
 
            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view
 
            return vhItem; // Returning the created object
 
            //inflate your layout and pass it to view holder
 
        } else if (viewType == TYPE_HEADER) {
 
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout
 
            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view
 
            return vhHeader; //returning the object created
 
             
        }
        return null;
 
    }
 
    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
                                                              // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons


            holder.setClickListener(new MyAdapter.ViewHolder.ClickListener() {
                public void onClick(View v, int pos, boolean isLongClick) {
                    if (isLongClick) {

                        // View v at position pos is long-clicked.
                        Toast.makeText(mContext, "longclick ", Toast.LENGTH_SHORT).show();

                    } else {

                        switch (pos) {

                            case 1:

                                // View v at position pos is clicked.
                                Intent i = new Intent(mContext, EmailPasswordActivity.class);
                                v.getContext().startActivity(i);

                                break;

                            case 2:

                                // View v at position pos is clicked.
                                Intent i2 = new Intent(mContext, IntsActivity.class);
                                v.getContext().startActivity(i2);

                                break;

                            case 3:

                                // View v at position pos is clicked.
                                Intent i3 = new Intent(mContext, MapActivity.class);
                                v.getContext().startActivity(i3);

                                break;

                            case 4:

                                // View v at position pos is clicked.
                                Intent i4 = new Intent(mContext, SettingsActivity.class);
                                v.getContext().startActivity(i4);

                                break;


                            default:
                                break;
                        }

                    }
                }
            });




        }
        else{
 
            holder.profile.setImageResource(profile);           // Similarly we set the resources for header view
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }
 
    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }
 
     
    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) { 
        if (isPositionHeader(position))
            return TYPE_HEADER;
 
        return TYPE_ITEM;
    }
 
    private boolean isPositionHeader(int position) {
        return position == 0;
    }
 
}