package hanu.a2_1901040179.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cart.db";
    private static final int VERSION = 1;
    public DbHelper(@Nullable Context context ) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cart("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "productId INTEGER NOT NULL," +
                        "name TEXT NOT NULL," +
                        "price DOUBLE NOT NULL, "+
                        "imgUrl TEXT NOT NULL," +
                        "quantity INTEGER  )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE cart");

        onCreate(db);
    }
}
