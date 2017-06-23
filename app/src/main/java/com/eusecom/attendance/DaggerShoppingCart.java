package com.eusecom.attendance;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.eusecom.attendance.dagger.LineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentine on 4/20/2016.
 */


public class DaggerShoppingCart {
    private final SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private List<LineItem> shoppingCart;

    private static final String OPEN_CART_EXITS = "open_cart_exists";
    private static final String SERIALIZED_CART_ITEMS = "serialized_cart_items";
    private static final String SERIALIZED_CUSTOMER = "serialized_customer";



    public DaggerShoppingCart(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        editor = sharedPreferences.edit();
        initShoppingCart();
    }

    private void initShoppingCart() {
        shoppingCart = new ArrayList<>();

        Gson gson = new Gson();

        if (sharedPreferences.getBoolean(OPEN_CART_EXITS, false)){
            String serializedCartItems = sharedPreferences.getString(SERIALIZED_CART_ITEMS,"");
            String serializedCustomer = sharedPreferences.getString(SERIALIZED_CUSTOMER,"");
            if (!serializedCartItems.equals("")){
                shoppingCart = gson.<ArrayList<LineItem>>fromJson(serializedCartItems,
                        new TypeToken<ArrayList<LineItem>>(){}.getType());
            }


        }
        updateApp();


    }

    public void addItemToCart(LineItem item){
        Log.d("cart addItem ", item.getProductName());
        if (shoppingCart.contains(item)){
            int currentPosition = shoppingCart.indexOf(item);
            LineItem itemAlreadyInCart = shoppingCart.get(currentPosition);
            itemAlreadyInCart.setQuantity(itemAlreadyInCart.getQuantity() + item.getQuantity());
            shoppingCart.set(currentPosition, itemAlreadyInCart);
        }else {
            shoppingCart.add(item);
        }

    }

    public void clearShoppingCart(){
        shoppingCart.clear();
        editor.putString(SERIALIZED_CART_ITEMS, "").commit();
        editor.putString(SERIALIZED_CUSTOMER, "").commit();
        editor.putBoolean(OPEN_CART_EXITS, false).commit();
        updateApp();
    }

    public void removeItemFromCart(int pos){
        shoppingCart.remove(pos);
        updateApp();
    }


    public void completeCheckout(){
        shoppingCart.clear();
        updateApp();
    }

    private void updateApp() {
       //perform any action that is needed to update the app
    }

    public List<LineItem> getShoppingCart() {
        return shoppingCart;
    }



    public void saveCartToPreference(){
        Log.d("cart save ", "cart");
        if (shoppingCart != null) {
            Gson gson = new Gson();
            String serializedItems = gson.toJson(shoppingCart);
            editor.putString(SERIALIZED_CART_ITEMS, serializedItems).commit();
            editor.putBoolean(OPEN_CART_EXITS, true).commit();
        }
    }

    public void updateItemQty(LineItem item, int qty) {

        boolean itemAlreadyInCart = shoppingCart.contains(item);

        if (itemAlreadyInCart) {
            int position = shoppingCart.indexOf(item);
            LineItem itemInCart = shoppingCart.get(position);
            itemInCart.setQuantity(qty);
            shoppingCart.set(position, itemInCart);
        } else {
            item.setQuantity(qty);
            shoppingCart.add(item);
        }

        updateApp();

    }
}
