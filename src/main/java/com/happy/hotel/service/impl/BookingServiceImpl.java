package com.happy.hotel.service.impl;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BookingResponse;
import com.happy.hotel.entity.BookingEntity;
import com.happy.hotel.enums.BookingStatus;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.BookingReposiotry;
import com.happy.hotel.service.BookingService;
import com.happy.hotel.service.MailSenderService;
import com.happy.hotel.service.PaymentService;
import com.happy.hotel.service.RoomService;
import com.happy.hotel.utils.CurrencyConverter;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private RoomService roomService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private BookingReposiotry bookingReposiotry;
	@Autowired
	private MailSenderService mailSenderService;
	private final static double BASE_PRICE_USD = 50.0;

	@Override
	public int getAvailablePlaceCount() {
		return roomService.getAvailableRooms().stream().map(room -> room.getCapacity()).reduce(0, Integer::sum);
	}

	@Override
	public double calculatePrice(BookingRequest bookingRequest) {
		long nights = ChronoUnit.DAYS.between(bookingRequest.getDateFrom(), bookingRequest.getDateTo());
		return BASE_PRICE_USD * bookingRequest.getGuestCount() * nights;
	}

	@Override
	public double calculatePriceEuro(BookingRequest bookingRequest) {
		long nights = ChronoUnit.DAYS.between(bookingRequest.getDateFrom(), bookingRequest.getDateTo());
		return CurrencyConverter.toEuro(BASE_PRICE_USD * bookingRequest.getGuestCount() * nights);
	}

	@Override
	public Integer makeBooking(BookingRequest bookingRequest) {
		Integer roomId = roomService.findAvailableRoomId(bookingRequest);
		double price = calculatePrice(bookingRequest);
		if (bookingRequest.getPrepaid()) {
			paymentService.pay(bookingRequest, price);
		}
		BookingEntity booking = new BookingEntity();
		booking.setCustomerId(bookingRequest.getCustomerId());
		booking.setDateFrom(bookingRequest.getDateFrom());
		booking.setDateTo(bookingRequest.getDateTo());
		booking.setGuestCount(bookingRequest.getGuestCount());
		booking.setPrepaid(bookingRequest.getPrepaid());
		booking.setRoomId(roomId);
		booking.setUserId(bookingRequest.getUserId());
		booking.setStatus(BookingStatus.BOOKED);
		bookingReposiotry.save(booking);
		roomService.bookRoom(roomId);
		mailSenderService.sendBookingConfirmation(booking.getId());
		return booking.getId();
	}

	@Override
	public void cancelBooking(Integer id) {
		BookingEntity booking = bookingReposiotry.findById(id)
				.orElseThrow(() -> new HappyHotelException(Msg.BOOKING_NOT_FOUND));
		roomService.unbookRoom(booking.getRoomId());
		booking.setStatus(BookingStatus.CANCELED);
		bookingReposiotry.save(booking);

	}

	@Override
	public void closeBooking(Integer id) {
		BookingEntity booking = bookingReposiotry.findById(id)
				.orElseThrow(() -> new HappyHotelException(Msg.BOOKING_NOT_FOUND));
		roomService.unbookRoom(booking.getRoomId());
		booking.setStatus(BookingStatus.CLOSED);
		bookingReposiotry.save(booking);

	}

	@Override
	public BookingResponse getById(Integer bookingId) {
		Optional<BookingEntity> optional = bookingReposiotry.findById(bookingId);
		if (!optional.isPresent()) {
			return null;
		}
		BookingEntity entity = optional.get();
		BookingResponse booking = new BookingResponse();
		booking.setCustomerId(entity.getCustomerId());
		booking.setDateFrom(entity.getDateFrom());
		booking.setDateTo(entity.getDateTo());
		booking.setGuestCount(entity.getGuestCount());
		booking.setPrepaid(entity.getPrepaid());
		booking.setRoomId(entity.getRoomId());
		booking.setUserId(entity.getUserId());
		booking.setStatus(entity.getStatus());
		booking.setId(entity.getId());
		booking.setCreated(entity.getCreated());
		return booking;
	}

}
