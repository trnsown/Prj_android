package hanu.a2_1901040179.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1901040179.R;
import hanu.a2_1901040179.db.CartManager;
import hanu.a2_1901040179.models.CartItem;
import hanu.a2_1901040179.models.Constants;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {

    List<CartItem> cart;
    private Context context;

    ItemsChangeListener listener;

    public CartAdapter(List<CartItem> cart, ItemsChangeListener listener) {
        this.cart = cart;
        this.listener = listener;
        notifyDataSetChanged();
    }


    protected  class CartHolder extends RecyclerView.ViewHolder  {

        long cartItemId;
        int productId;
        String name;
        double price;
        int quantity;
        long finalPrice = 0 ;
        CartManager cartManager;
        CartItem item;
        ItemsChangeListener listener;
        ImageButton addBtn = itemView.findViewById(R.id.btnAdd);
        ImageButton removeBtn = itemView.findViewById(R.id.btnDelete);
        TextView tvName = itemView.findViewById(R.id.tvNameCart);
        TextView tvPrice = itemView.findViewById(R.id.tvPriceCart);
        TextView tvPriceSum = itemView.findViewById(R.id.tvPriceSum);
        TextView tvQuantity = itemView.findViewById(R.id.txtQuantity);
        ImageView imgThumbnail = itemView.findViewById(R.id.imgProductCart);



        public CartHolder(@NonNull View itemView, ItemsChangeListener listener) {
            super(itemView);
            this.listener = listener;

        }

        public void bind(CartItem cartItem) {

            productId = cartItem.getProductId();
            name = cartItem.getName();
            price = cartItem.getPrice();
            double priceSum = cartItem.getPrice() * cartItem.getQuantity();
            quantity = cartItem.getQuantity();

            cartManager = CartManager.getInstance(itemView.getContext());




            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartItem.setQuantity(cartItem.getQuantity()+1);
                    cartManager.updateCart(cartItem.getProductId(), cartItem.getQuantity());
                    tvQuantity.setText(String.valueOf(quantity));

                    notifyDataSetChanged();
                    finalPrice = 0;
                    for (CartItem c : cartManager.all()) {
                        finalPrice = (long) ( finalPrice + c.getPrice() * c.getQuantity());
                    }
                    listener.onItemClick(finalPrice);
                }
            });

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartItem.setQuantity(cartItem.getQuantity()-1);
                    cartManager.updateCart(cartItem.getProductId(), cartItem.getQuantity());
                    tvQuantity.setText(String.valueOf(quantity));
                    if (cartItem.getQuantity() == 0 ) {
                        cartManager.deleteCart(cartItem.getId());
                        cart.remove(cartItem);
                    }

                    notifyDataSetChanged();
                    finalPrice = 0;
                    for (CartItem c : cartManager.all()) {
                        finalPrice = (long) ( finalPrice + c.getPrice() * c.getQuantity());
                    }
                    listener.onItemClick(finalPrice);
                }

            });

            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = loadImg(cartItem.getImageUrl());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imgThumbnail.setImageBitmap(bm);
                        }
                    });
                }
            });
            tvName.setText(name);
            tvPrice.setText("VND " +  (long)price);
            tvPriceSum.setText("VND " + (long)priceSum );
            tvQuantity.setText(String.valueOf(quantity));


        }

    }

    //data set
    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_cart,  parent, false);
        return new CartHolder(item,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        CartItem cartItem = cart.get(position);

        holder.bind(cartItem);

    }

    public interface ItemsChangeListener{
        void onItemClick(long totalPrice);
    }


    @Override
    public int getItemCount() {
        return cart.size();
    }

    public Bitmap loadImg(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();
            InputStream a = httpURLConnection.getInputStream();
            Bitmap bm = BitmapFactory.decodeStream(a);
            return  bm;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}


