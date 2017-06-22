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

public class DaggerMainActivity extends AppCompatActivity {

    DaggerShoppingCart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dagger_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.daggerdemo));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        cart = new DaggerShoppingCart(prefs);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Product product = new Product();
                product.setId(1);
                product.setProductName("Name1");
                product.setDescription("Desc1");
                LineItem item = new LineItem(product, 2);
                cart.addItemToCart(item);

                Product product2 = new Product();
                product2.setId(2);
                product2.setProductName("Name2");
                product2.setDescription("Desc2");
                LineItem item2 = new LineItem(product2, 5);

                cart.addItemToCart(item2);
                cart.saveCartToPreference();

                String carts0 = cart.getShoppingCart().get(0).getProductName().toString();
                String carts1 = cart.getShoppingCart().get(1).getProductName().toString();
                Snackbar.make(view, "carts names " + carts0 + " " + carts1, Snackbar.LENGTH_LONG).setAction("Action", null).show();

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

            String cartsx0="", cartsx1="";
            try {
                cartsx0 = cart.getShoppingCart().get(0).getProductName().toString() + " " +
                        cart.getShoppingCart().get(0).getQuantity();
            }catch( IndexOutOfBoundsException e){ }

            try {
                cartsx1 = cart.getShoppingCart().get(1).getProductName().toString() + " " +
                    cart.getShoppingCart().get(1).getQuantity();
            }catch( IndexOutOfBoundsException e){ }


            String toastx = "carts names " + cartsx0 + " " + cartsx1;

            Toast.makeText(this, toastx,Toast.LENGTH_LONG).show();



            return true;
        }
        if (id == R.id.daggeradd) {

            Product product = new Product();
            product.setId(3);
            product.setProductName("Name3");
            product.setDescription("Desc3");
            LineItem itemdel = new LineItem(product, 7);
            cart.addItemToCart(itemdel);
            cart.saveCartToPreference();

            return true;
        }
        if (id == R.id.daggerdel) {

            Product product = new Product();
            product.setId(1);
            product.setProductName("Name1");
            product.setDescription("Desc1");
            LineItem itemdel = new LineItem(product, 2);
            cart.removeItemFromCart(itemdel);
            cart.saveCartToPreference();

            return true;
        }
        if (id == R.id.daggerclear) {

            cart.clearShoppingCart();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
