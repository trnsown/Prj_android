package hanu.a2_1901040179.adapters;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

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
import hanu.a2_1901040179.models.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    List<Product> products;
    Intent intent;


    public ProductAdapter(List<Product> products) {
        this.products = products;
    }



    //view holder
    protected  class ProductHolder extends RecyclerView.ViewHolder {

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Product product) {
            TextView tvName = itemView.findViewById(R.id.tvName);
            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
            ImageView imgThumbnail = itemView.findViewById(R.id.imgProduct);

            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap bm = loadImg(product.getImageUrl());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imgThumbnail.setImageBitmap(bm);
                        }
                    });
                }
            });
            tvName.setText(product.getName());
            tvPrice.setText("VND " + (long) product.getPrice());

            ImageButton imgButton = itemView.findViewById(R.id.btnAddCart);

            imgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartManager cartManager = CartManager.getInstance(view.getContext());
                    boolean exist = false;
                    int quantity = 1;


                    for (CartItem item : cartManager.all()) {
//                        quantity = item.getQuantity();
                        if(item.getProductId() == product.getId()){
                           item.setQuantity(item.getQuantity()+1);
                           exist= true;
                           cartManager.updateCart(item.getProductId(), item.getQuantity());

                        }
                    }
                    if (!exist) {
                        CartItem cartItem = new CartItem(product.getId(),product.getName(),product.getPrice(),product.getImageUrl(), 1);
                        cartManager.addCart(cartItem);
                    }
                    Toast.makeText(view.getContext(), "Added Successfully!", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();


                }
            });

        }
    }

    //data set
    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.item_product,  parent, false);
        return new ProductHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);

    }

    @Override
    public int getItemCount() {
        return products.size();
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
