package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.eusecom.attendance.dagger.LineItem;
import com.eusecom.attendance.dagger.Product;

import javax.inject.Inject;

public class DaggerMainActivity extends AppCompatActivity {

    @Inject SharedPreferences sharedPreferences;
    DaggerShoppingCart cart;
    DaggerProductListener daggerProductListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dagger_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.daggerdemo));

        AttendanceApplication.getInstance().getAppComponent().inject(this);
        //i do not need SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());, i take it from dagger
        cart = new DaggerShoppingCart(sharedPreferences);
        daggerProductListener = new DaggerProductListener();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dagger_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.daggershow) {

            String cartsx0="", cartsx1="", cartsx2="";
            try {
                //cartsx0 = cart.getShoppingCart().get(0).getProductName().toString() + " " +
                //        cart.getShoppingCart().get(0).getQuantity();

                cartsx0 = daggerProductListener.onShowButtonClicked().get(0).getProductName().toString() + " " +
                        daggerProductListener.onShowButtonClicked().get(0).getQuantity();

            }catch( IndexOutOfBoundsException e){ }

            try {
                //cartsx1 = cart.getShoppingCart().get(1).getProductName().toString() + " " +
                //    cart.getShoppingCart().get(1).getQuantity();

                cartsx1 = daggerProductListener.onShowButtonClicked().get(1).getProductName().toString() + " " +
                        daggerProductListener.onShowButtonClicked().get(1).getQuantity();

            }catch( IndexOutOfBoundsException e){ }

            try {

                cartsx2 = daggerProductListener.onShowButtonClicked().get(2).getProductName().toString() + " " +
                        daggerProductListener.onShowButtonClicked().get(2).getQuantity();

            }catch( IndexOutOfBoundsException e){ }


            String toastx = "carts names " + cartsx0 + " " + cartsx1 + " " + cartsx2;

            Toast.makeText(this, toastx,Toast.LENGTH_LONG).show();



            return true;
        }
        if (id == R.id.daggeradd) {

            Product product = new Product();
            product.setId(1);
            product.setProductName("Name1");
            product.setDescription("Desc1");
            LineItem itemdel = new LineItem(product, 7);

            daggerProductListener.onAddToCartButtonClicked(product);

            //cart.addItemToCart(itemdel);
            //cart.saveCartToPreference();

            return true;
        }

        if (id == R.id.daggeradd2) {

            Product product = new Product();
            product.setId(2);
            product.setProductName("Name2");
            product.setDescription("Desc2");
            LineItem itemdel = new LineItem(product, 7);

            daggerProductListener.onAddToCartButtonClicked(product);

            //cart.addItemToCart(itemdel);
            //cart.saveCartToPreference();

            return true;
        }

        if (id == R.id.daggeradd3) {

            Product product = new Product();
            product.setId(3);
            product.setProductName("Name3");
            product.setDescription("Desc3");
            LineItem itemdel = new LineItem(product, 7);

            daggerProductListener.onAddToCartButtonClicked(product);

            //cart.addItemToCart(itemdel);
            //cart.saveCartToPreference();

            return true;
        }
        if (id == R.id.daggerdel) {

            daggerProductListener.onDeleteItemButtonClicked(0);

            //cart.removeItemFromCart(0);
            //cart.saveCartToPreference();

            return true;
        }
        if (id == R.id.daggerclear) {

            daggerProductListener.onClearButtonClicked();

            //cart.clearShoppingCart();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
