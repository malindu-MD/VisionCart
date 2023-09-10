package com.example.visioncart.models;

public class VolunteerRequest {


    String id;
    String  name;

    String phoneNumber;
    String date;

    String time;

    String message;

    String volunteer;

    String status;

    public VolunteerRequest(){

    }

    public VolunteerRequest(String ID, String name, String phoneNumber, String date, String time, String message, String volunteer, String status) {
        this.id = ID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.message = message;
        this.volunteer = volunteer;
        this.status = status;
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
