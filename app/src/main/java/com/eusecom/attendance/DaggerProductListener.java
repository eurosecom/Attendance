package com.eusecom.attendance;

import android.util.Log;

import com.eusecom.attendance.dagger.LineItem;
import com.eusecom.attendance.dagger.Product;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Valentine on 4/20/2016.
 */
public class DaggerProductListener {

    //We are creating a class member variable for the
    //Shopping cart that we will be injecting to this class
    @Inject
    DaggerShoppingCart mCart;

    public DaggerProductListener(){
        //Here is where the actual injection takes place
        AttendanceApplication.getInstance().getAppComponent().inject(this);
    }

    //Here is an example of how we are using the injected shopping cart
    public void onItemQuantityChanged(LineItem item, int qty) {
        mCart.updateItemQty(item, qty);
    }

    //Another example of using the shopping cart
    public void onAddToCartButtonClicked(Product product) {
        //perform add to checkout button here
        LineItem item = new LineItem(product, 1);
        Log.d("list onAdd ", item.getProductName());
        mCart.addItemToCart(item);
        mCart.saveCartToPreference();
    }

    public void onClearButtonClicked() {
        mCart.clearShoppingCart();
    }

    public List<LineItem> onShowButtonClicked() {
        return mCart.getShoppingCart();
    }

    public void onDeleteItemButtonClicked(int pos) {
        mCart.removeItemFromCart(pos);
    }
}
