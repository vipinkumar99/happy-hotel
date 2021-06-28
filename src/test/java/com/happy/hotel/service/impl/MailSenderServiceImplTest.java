package com.happy.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.happy.hotel.service.MailSenderService;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

@SpringBootTest
class MailSenderServiceImplTest {

	@Autowired
	private MailSenderService mailSenderService;

	private ListAppender<ILoggingEvent> appenders;

	@BeforeEach
	public void setup() {
		Logger logger = (Logger) LoggerFactory.getLogger(MailSenderServiceImpl.class.getName());
		appenders = new ListAppender<>();
		appenders.start();
		logger.addAppender(appenders);
	}

	@Test
	public void shouldSendBookingConfirmationTest() {
		mailSenderService.sendBookingConfirmation(1);
		List<ILoggingEvent> logsEvents = appenders.list;
		String expected = "Hi, Welcome to Happy Hotel. This is your booking number:1";
		assertEquals(expected, logsEvents.get(0).getMessage());
		assertEquals(Level.INFO, logsEvents.get(0).getLevel());
	}

}
