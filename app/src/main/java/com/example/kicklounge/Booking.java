package com.example.kicklounge;

public class Booking {
    public String fieldName, firstName, lastName, email, phone, date, time, amount, payment;

    public String firebaseKey;
    public String userUid;
    public Booking() { }

    public Booking(String fieldName, String firstName, String lastName, String email, String phone, String date, String time, String amount, String payment) {
        this.fieldName = fieldName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.payment = payment;
    }
}
