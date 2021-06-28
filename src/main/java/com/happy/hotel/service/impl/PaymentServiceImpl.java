package com.happy.hotel.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.entity.PaymentEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.PaymentRepository;
import com.happy.hotel.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Override
	public Integer pay(BookingRequest bookingRequest, double price) throws HappyHotelException {
		if (price > 200.0 && bookingRequest.getGuestCount() < 3) {
			throw new HappyHotelException(Msg.ONLY_SMALL_PAYMENTS_SUPPORTED);
		}
		PaymentEntity entity = new PaymentEntity();
		entity.setPrice(price);
		paymentRepository.save(entity);
		return entity.getId();
	}

}
