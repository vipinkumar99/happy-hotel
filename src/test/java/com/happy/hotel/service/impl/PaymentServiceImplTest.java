package com.happy.hotel.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.entity.PaymentEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.PaymentRepository;
import com.happy.hotel.service.PaymentService;

@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	private PaymentService paymentService;

	@MockBean
	private PaymentRepository paymentRepository;

	private PaymentEntity paymentEntity;

	@Captor
	private ArgumentCaptor<PaymentEntity> payentCaptor;

	@BeforeEach
	public void setup() {
		paymentEntity = new PaymentEntity();
		paymentEntity.setCreated(new Date());
		paymentEntity.setId(1);
		paymentEntity.setPrice(400.0);
	}

	@Test
	public void paymentShouldThrowExceptionTest() {
		assertThrows(HappyHotelException.class, () -> paymentService.pay(getBookingRequest(2), 400.0));
		verify(paymentRepository, never()).save(Mockito.any(PaymentEntity.class));
	}

	@Test
	public void paymentShouldSaveTest1() {
		when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(paymentEntity);
		paymentService.pay(getBookingRequest(4), 400.0);
		verify(paymentRepository).save(payentCaptor.capture());
	}

	@Test
	public void paymentShouldSaveTest2() {
		when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(paymentEntity);
//		assertEquals(1, paymentService.pay(getBookingRequest(4), 300.0));
		paymentService.pay(getBookingRequest(4), 300.0);
		verify(paymentRepository, times(1)).save(Mockito.any(PaymentEntity.class));
	}

	@Test
	public void paymentShouldSaveTest3() {
		when(paymentRepository.save(Mockito.any(PaymentEntity.class))).thenReturn(paymentEntity);
	//	assertEquals(1, paymentService.pay(getBookingRequest(2), 100.0));
		paymentService.pay(getBookingRequest(2), 100.0);
		verify(paymentRepository, times(1)).save(Mockito.any(PaymentEntity.class));
	}

	public static BookingRequest getBookingRequest(int guestCount) {
		BookingRequest request = new BookingRequest();
		request.setUserId(1);
		request.setDateFrom(LocalDate.now());
		request.setDateTo(LocalDate.now().plusDays(1));
		request.setGuestCount(guestCount);
		request.setPrepaid(false);
		request.setCustomerId(1);
		request.setRoomId(1);
		return request;
	}
}
