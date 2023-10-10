package org.tensorflow.blindhelp.examples.classification.models;

public class Offers {
    String pname;
    String id;
    String dateStart;
    String dateEnd;

    String offerDetails;

    public Offers() {
        // Default constructor required by Firebase
    }

    public Offers(String pname, String id, String dateStart, String dateEnd, String offerDetails) {
        this.pname = pname;
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.offerDetails = offerDetails;
    }

    public String getPname() {
        return pname;
    }

    public String getId() {
        return id;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public String getOfferDetails() {
        return offerDetails;
    }
}
