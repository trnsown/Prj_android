package hanu.a2_1901040179.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040179.models.CartItem;


public class CartCursorWrapper extends CursorWrapper {
    public CartCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CartItem getItem() {
        long itemId = getLong(getColumnIndex(DbSchema.CartTable.Columns.ID));
        int productId = getInt(getColumnIndex(DbSchema.CartTable.Columns.PRODUCT_ID));
        String name = getString(getColumnIndex(DbSchema.CartTable.Columns.NAME));
        double price = getDouble(getColumnIndex(DbSchema.CartTable.Columns.PRICE));
        String imgUrl = getString(getColumnIndex(DbSchema.CartTable.Columns.IMGURL));
        int quantity = getInt(getColumnIndex(DbSchema.CartTable.Columns.QUANTITY));


        CartItem product = new CartItem(itemId,productId,name,price,imgUrl,quantity);

        return product;
    }

    public List<CartItem> getCart () {
        List<CartItem> cart = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            CartItem cartItem = getItem();
            cart.add(cartItem);
            moveToNext();
        }
        return  cart;
    }
}
