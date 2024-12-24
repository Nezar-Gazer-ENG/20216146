package com.sdaproject.api20216146.util;

public class BookingMessageTemplate {

    public static String getHotelBookingMessage(String hotelName, String checkInDate) {
        return "You have successfully booked in with " + hotelName + " and your check-in date is on " + checkInDate;
    }

    public static String getEventBookingMessage(String eventName, String eventDate) {
        return "You have successfully booked into the event " + eventName + " and its date will be on " + eventDate;
    }
}
