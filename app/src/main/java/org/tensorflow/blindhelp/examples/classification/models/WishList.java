package org.tensorflow.blindhelp.examples.classification.models;

public class WishList {

    String detaillsID;
    String QuantityID;

    public WishList(){

    }

    public String getDetaillsID() {
        return detaillsID;
    }

    public String getQuantityID() {
        return QuantityID;
    }

    public WishList(String detaillsID, String quantityID) {
        this.detaillsID = detaillsID;
        QuantityID = quantityID;
    }
}
