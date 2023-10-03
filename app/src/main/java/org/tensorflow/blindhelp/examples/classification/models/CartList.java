package org.tensorflow.blindhelp.examples.classification.models;

public class CartList {

    private String key; // Add a key property
    String cartList;

    public CartList(){}

    public String getCartList() {
        return cartList;
    }

    public CartList(String cartList) {
        this.cartList = cartList;
    }

    // Getter and setter for the key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
