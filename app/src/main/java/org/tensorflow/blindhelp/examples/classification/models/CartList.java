package org.tensorflow.blindhelp.examples.classification.models;

public class CartList {

    private String key; // Add a key property
    // String cartList;
    String name;
    String details;

    public CartList(String name, String details, String price, String mfg, String exp) {
        this.name = name;
        this.details = details;
        this.price = price;
        this.mfg = mfg;
        this.exp = exp;
    }

    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMfg() {
        return mfg;
    }

    public void setMfg(String mfg) {
        this.mfg = mfg;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    String mfg;
    String exp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public CartList(){}



//   public String getCartList() {
//        return cartList;
//    }
//
//    public CartList(String cartList) {
//        this.cartList = cartList;
//    }

    // Getter and setter for the key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
