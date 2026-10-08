package com.example.kicklounge;

public class BookingWithKey {
    public String key;
    public String userUid;
    public Booking booking;

    public BookingWithKey() {}

    public BookingWithKey(String key, String userUid, Booking booking) {
        this.key = key;
        this.userUid = userUid;
        this.booking = booking;
    }
}
