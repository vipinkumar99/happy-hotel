package com.happy.hotel.service;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.exception.HappyHotelException;

public interface PaymentService {
	Integer pay(BookingRequest bookingRequest, double price) throws HappyHotelException;
}
