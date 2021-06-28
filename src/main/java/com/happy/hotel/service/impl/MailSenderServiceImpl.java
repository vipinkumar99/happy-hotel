package com.happy.hotel.service.impl;

import org.springframework.stereotype.Service;

import com.happy.hotel.service.MailSenderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailSenderServiceImpl implements MailSenderService {

	@Override
	public void sendBookingConfirmation(Integer bookingId) {
		log.info("Hi, Welcome to Happy Hotel. This is your booking number:" + bookingId);
	}

}
