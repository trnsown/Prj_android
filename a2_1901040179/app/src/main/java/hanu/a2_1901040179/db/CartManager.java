package hanu.a2_1901040179.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;
import hanu.a2_1901040179.models.CartItem;

public class CartManager {
    private static  CartManager instance;
    private static final String DB_INSERT = "INSERT INTO "+DbSchema.CartTable.NAME+"(productId,name,price,imgUrl,quantity)VALUES(?,?,?,?,?)";
    private   SQLiteDatabase database;
    private DbHelper dbHelper;

    public static CartManager getInstance(Context context) {
        if ( instance == null) {
            instance = new CartManager(context);
        }
        return instance;
    }

    private CartManager(Context context) {
        dbHelper = new DbHelper(context);
        database =  dbHelper.getWritableDatabase();

    }

    public List<CartItem> all() {
        Cursor cs = database.rawQuery("SELECT * FROM cart",null);

        CartCursorWrapper cartCursorWrapper = new CartCursorWrapper(cs);

        return  cartCursorWrapper.getCart();
    }


    public boolean addCart (CartItem cartItem) {
        SQLiteStatement stm = database.compileStatement(DB_INSERT);
        stm.bindLong(1,cartItem.getProductId());
        stm.bindString(2,cartItem.getName());
        stm.bindDouble(3,cartItem.getPrice());
        stm.bindString(4,cartItem.getImageUrl());
        stm.bindLong(5,cartItem.getQuantity());

        long id = stm.executeInsert();


        if (id > 0 ) {
            cartItem.setId(id);
            return  true;
        }
        return  false;
    }
    public boolean deleteCart (Long id) {
        int rs = database.delete(DbSchema.CartTable.NAME, "id=?", new String[]{id+""});
        return rs>0;
    }
    public void updateCart (int productId, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        int rs = database.update(DbSchema.CartTable.NAME,contentValues, "productId = ?", new String[]{productId+""});
    }
}
