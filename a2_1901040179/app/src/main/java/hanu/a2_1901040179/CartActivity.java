package hanu.a2_1901040179;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_1901040179.adapters.CartAdapter;
import hanu.a2_1901040179.db.CartManager;
import hanu.a2_1901040179.models.CartItem;


public class CartActivity extends AppCompatActivity implements CartAdapter.ItemsChangeListener {

    public static final int CART_ADDED = 1;

    private RecyclerView rvCart;

    private List<CartItem> cart;

    private CartAdapter cartAdapter;

    private CartManager cartManager;

    long finalPrice= 0 ;

    int count =0;

    TextView tvCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setTitle(Html.fromHtml("<font color =\"black\">" + getString(R.string.app_name) + "</font>" ));

        //data set
        cartManager = CartManager.getInstance(CartActivity.this);
        cart = cartManager.all();


        tvCheckout = findViewById(R.id.txtPrice);



        //adapter
        cartAdapter = new CartAdapter(cart,this);

        //recycleview
        rvCart = findViewById(R.id.rvCart);
        cartAdapter.notifyDataSetChanged();


        for (CartItem item : cart) {
            finalPrice = (long) ( finalPrice + item.getPrice() * item.getQuantity());
        }
        tvCheckout.setText("VND" + finalPrice);

        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        Button checkout = findViewById(R.id.btnCheckout);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CART_ADDED) {
            cart.clear();
            cart.addAll(cartManager.all());
            cartAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.product) {

            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);

            return true;
        }


        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClick(long totalPrice) {

        tvCheckout.setText("VND "+totalPrice);
    }

}