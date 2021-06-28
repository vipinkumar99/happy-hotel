package com.happy.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BookingResponse;
import com.happy.hotel.dto.response.RoomResponse;
import com.happy.hotel.entity.BookingEntity;
import com.happy.hotel.enums.BookingStatus;
import com.happy.hotel.enums.RoomStatus;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.BookingReposiotry;
import com.happy.hotel.service.BookingService;
import com.happy.hotel.service.MailSenderService;
import com.happy.hotel.service.PaymentService;
import com.happy.hotel.service.RoomService;

@SpringBootTest
class BookingServiceImplTest {

	@Autowired
	private BookingService bookingService;
	@MockBean
	private RoomService roomService;
	@MockBean
	private PaymentService paymentService;
	@MockBean
	private MailSenderService mailSenderService;
	@MockBean
	private BookingReposiotry bookingReposiotry;

	private static BookingRequest bookingRequest;

	private static RoomResponse roomResponse;

	private static List<RoomResponse> roomResponseList;

	private static BookingEntity bookingEntity;

	private static final int BOOKING_ID = 1;

	@BeforeAll
	public static void init() {
		bookingEntity = new BookingEntity();
		bookingEntity.setId(BOOKING_ID);
		bookingEntity.setCreated(new Date());
		bookingEntity.setUserId(1);
		bookingEntity.setDateFrom(LocalDate.of(2020, 01, 01));
		bookingEntity.setDateTo(LocalDate.of(2020, 01, 05));
		bookingEntity.setGuestCount(2);
		bookingEntity.setPrepaid(false);
		bookingEntity.setRoomId(1);
		bookingEntity.setCustomerId(1);
		bookingEntity.setStatus(BookingStatus.BOOKED);

		bookingRequest = new BookingRequest();
		bookingRequest.setUserId(1);
		bookingRequest.setDateFrom(LocalDate.of(2020, 01, 01));
		bookingRequest.setDateTo(LocalDate.of(2020, 01, 05));
		bookingRequest.setGuestCount(5);
		bookingRequest.setPrepaid(false);
		bookingRequest.setRoomId(1);
		bookingRequest.setCustomerId(1);

		roomResponse = new RoomResponse();
		roomResponse.setCapacity(5);
		roomResponse.setStatus(RoomStatus.UNBOOKED);
		roomResponse.setCreated(new Date());
		roomResponse.setId(1);

		RoomResponse roomResponse2 = new RoomResponse();
		roomResponse2.setCapacity(2);
		roomResponse2.setStatus(RoomStatus.UNBOOKED);
		roomResponse2.setCreated(new Date());
		roomResponse2.setId(2);

		roomResponseList = Arrays.asList(roomResponse, roomResponse2);

	}

	@Test
	public void shouldReturnOneRoomAvailablePlaceCountWhenOneRoomAvailableTest() {
		when(roomService.getAvailableRooms()).thenReturn(Arrays.asList(roomResponse));
		assertEquals(5, bookingService.getAvailablePlaceCount());
		verify(roomService, times(1)).getAvailableRooms();
	}

	@Test
	public void shouldReturnAllAvailablePlaceCountWhenMultipleRoomAvailableTest() {
		when(roomService.getAvailableRooms()).thenReturn(roomResponseList);
		assertEquals(7, bookingService.getAvailablePlaceCount());
		verify(roomService, times(1)).getAvailableRooms();
	}

	@Test
	public void shouldReturnCalculatePriceTest() {
		double expected = 4 * 5 * 50.0;
		double actual = bookingService.calculatePrice(bookingRequest);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldReturnCalculatePriceEuroTest() {
		double expected = 50.0 * 5 * 4 * 0.85;
		double actual = bookingService.calculatePriceEuro(bookingRequest);
		assertEquals(expected, actual);
	}

	@Test
	public void shouldThrowExceptionRoomNotAvailableTest() {
		when(roomService.findAvailableRoomId(bookingRequest))
				.thenThrow(new HappyHotelException(Msg.ROOM_NOT_AVAILABLE));
		assertThrows(HappyHotelException.class, () -> bookingService.makeBooking(bookingRequest));
		verify(roomService, times(1)).findAvailableRoomId(bookingRequest);
	}

	@Test
	public void shouldNotCompleteBookingWhenPriceTooHighTest() {
		when(paymentService.pay(Mockito.any(), Mockito.anyDouble()))
				.thenThrow(new HappyHotelException(Msg.ONLY_SMALL_PAYMENTS_SUPPORTED));
		assertThrows(HappyHotelException.class, () -> bookingService.makeBooking(getBookingPrepaidRequest(true)));
		verify(paymentService, times(1)).pay(Mockito.any(), Mockito.anyDouble());
	}

	@Test
	public void shouldInvokePaymentWhenPrepaid() {
		bookingService.makeBooking(getBookingPrepaidRequest(true));
		// verify(paymentService, times(1)).pay(getBookingPrepaidRequest(true), 400.0);
		// verifyNoMoreInteractions(paymentService);
	}

	@Test
	public void shouldNotInvokePaymentWhenNotPrepaid() {
		bookingService.makeBooking(getBookingPrepaidRequest(false));
		verify(paymentService, never()).pay(getBookingPrepaidRequest(false), 400.0);
	}

	@Test
	public void shouldMailSendTest() {
		doNothing().when(mailSenderService).sendBookingConfirmation(Mockito.any());
		bookingService.makeBooking(bookingRequest);
	}

	@Test
	public void shouldRoomBookTest() {
		doNothing().when(mailSenderService).sendBookingConfirmation(Mockito.any());
		doNothing().when(roomService).bookRoom(Mockito.anyInt());
		bookingService.makeBooking(bookingRequest);
	}

	@Test
	public void shouldReturnBookingByIdTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.of(bookingEntity));
		BookingResponse booking = bookingService.getById(BOOKING_ID);
		assertNotNull(booking);
		assertEquals(BookingStatus.BOOKED, booking.getStatus());
		verify(bookingReposiotry, times(1)).findById(BOOKING_ID);
		verifyNoMoreInteractions(bookingReposiotry);
	}

	@Test
	public void shouldReturnNullBookingByIdTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.empty());
		assertThat(bookingService.getById(BOOKING_ID)).isNull();
		verify(bookingReposiotry, times(1)).findById(BOOKING_ID);
		verifyNoMoreInteractions(bookingReposiotry);
	}

	@Test
	public void shouldThrowBookingNotFoundExceptionCancelBookingTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> bookingService.cancelBooking(BOOKING_ID));
		verify(bookingReposiotry, times(1)).findById(BOOKING_ID);
		verifyNoMoreInteractions(bookingReposiotry);
	}

	@Test
	public void shouldCancelBookingTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.of(bookingEntity));
		doNothing().when(roomService).unbookRoom(Mockito.anyInt());
		when(bookingReposiotry.save(Mockito.any(BookingEntity.class))).thenReturn(bookingEntity);
		bookingService.cancelBooking(BOOKING_ID);
		verify(bookingReposiotry, times(1)).findById(Mockito.anyInt());
		verify(bookingReposiotry, times(1)).save(Mockito.any(BookingEntity.class));
		verifyNoMoreInteractions(bookingReposiotry);
	}

	@Test
	public void shouldBookingNotFoundcCloseBookingTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> bookingService.closeBooking(BOOKING_ID));
		verify(bookingReposiotry, times(1)).findById(BOOKING_ID);
		verifyNoMoreInteractions(bookingReposiotry);
	}

	@Test
	public void shouldCloseBookingTest() {
		when(bookingReposiotry.findById(BOOKING_ID)).thenReturn(Optional.of(bookingEntity));
		doNothing().when(roomService).unbookRoom(Mockito.anyInt());
		bookingService.closeBooking(BOOKING_ID);
		verify(bookingReposiotry, times(1)).findById(BOOKING_ID);
	}

	private static BookingRequest getBookingPrepaidRequest(boolean prepaid) {
		BookingRequest bookingRequest = new BookingRequest();
		bookingRequest.setUserId(1);
		bookingRequest.setDateFrom(LocalDate.of(2020, 01, 01));
		bookingRequest.setDateTo(LocalDate.of(2020, 01, 05));
		bookingRequest.setGuestCount(2);
		bookingRequest.setPrepaid(prepaid);
		bookingRequest.setRoomId(1);
		bookingRequest.setCustomerId(1);
		return bookingRequest;
	}
}
