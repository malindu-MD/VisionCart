package org.tensorflow.blindhelp.examples.classification.models;

public class Products {

    String pName,pMfgDate,pExpDate,pPrice,pDetails;

    Products(){

    }

    public Products(String pName, String pMfgDate, String pExpDate, String pPrice, String pDetails) {
        this.pName = pName;
        this.pMfgDate = pMfgDate;
        this.pExpDate = pExpDate;
        this.pPrice = pPrice;
        this.pDetails = pDetails;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpMfgDate() {
        return pMfgDate;
    }

    public void setpMfgDate(String pMfgDate) {
        this.pMfgDate = pMfgDate;
    }

    public String getpExpDate() {
        return pExpDate;
    }

    public void setpExpDate(String pExpDate) {
        this.pExpDate = pExpDate;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpDetails() {
        return pDetails;
    }

    public void setpDetails(String pDetails) {
        this.pDetails = pDetails;
    }
}
