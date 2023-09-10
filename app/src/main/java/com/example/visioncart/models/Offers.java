package com.example.visioncart.models;

public class Offers {
    String pname;
    String id;
    String fromMonth;
    String fromday;
    String toMonth;
    String toDay;
    String offerDetails;

    public Offers() {
        // Default constructor required by Firebase
    }

    public Offers(String pname, String fromMonth, String fromday, String toMonth, String toDay, String offerDetails,String id) {
        this.pname = pname;
        this.fromMonth = fromMonth;
        this.fromday = fromday;
        this.toMonth = toMonth;
        this.toDay = toDay;
        this.offerDetails = offerDetails;
        this.id= id;
    }
    public String getPname() {
        return pname;
    }

    public String getId() {
        return id;
    }

    public String getFromMonth() {
        return fromMonth;
    }

    public String getFromday() {
        return fromday;
    }

    public String getToMonth() {
        return toMonth;
    }

    public String getToDay() {
        return toDay;
    }

    public String getOfferDetails() {
        return offerDetails;
    }




}
