package hanu.a2_1901040179.models;

import java.util.List;

public class Cart {

    private List<CartItem> items;


    public Cart(List<CartItem> items) {
        this.items = items;

    }

    public List<CartItem> getItems() {

        return items;
    }

    public void setItems(List<CartItem> items) {

        this.items = items;
    }


    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items +
                '}';
    }
}
