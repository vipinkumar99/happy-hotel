package com.happy.hotel.service;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BookingResponse;

public interface BookingService {
int getAvailablePlaceCount();
double calculatePrice(BookingRequest bookingRequest);
double calculatePriceEuro(BookingRequest bookingRequest);
Integer makeBooking(BookingRequest bookingRequest);
void cancelBooking(Integer id);
void closeBooking(Integer id);
BookingResponse getById(Integer bookingId);
}
