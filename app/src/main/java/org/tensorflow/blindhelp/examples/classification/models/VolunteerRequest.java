package org.tensorflow.blindhelp.examples.classification.models;

public class VolunteerRequest extends Blind {


    String id;

    String requestId;
    String  name;
    String email;
    String phoneNumber;
    String date;

    String time;

    String message;

    String volunteer;

    String volunteerName;

    String status;

    int point;

    public String getRequestId() {
        return requestId;
    }

    public VolunteerRequest(String id, String requestId, String email, String name, String phoneNumber, String date, String time, String message, String volunteer,String volunteerName, String status, int point) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.message = message;
        this.volunteer = volunteer;
        this.status = status;
        this.point = point;
        this.email=email;
        this.requestId=requestId;
        this.volunteerName=volunteerName;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public int getPoint() {
        return point;
    }

    public String getEmail() {
        return email;
    }

    public VolunteerRequest(){

    }


    public VolunteerRequest(String ID,String volunteer,String status){

        this.id=ID;
        this.volunteer=volunteer;
        this.status=status;

    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }


    public String getStatus() {
        return status;
    }

    public String getVolunteer() {
        return volunteer;
    }




}
