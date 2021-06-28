package com.happy.hotel.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.BookingResponse;
import com.happy.hotel.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@PostMapping(value = "/makeBooking")
	public ResponseEntity<?> makeBooking(@RequestBody BookingRequest request) {
		Integer response = bookingService.makeBooking(request);
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@PostMapping(value = "/checkPrice")
	public ResponseEntity<?> checkBookingPrice(@RequestBody BookingRequest request) {
		double response = bookingService.calculatePrice(request);
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping("/cancelBooking/{id}")
	public ResponseEntity<?> cancelBooking(@PathVariable("id") Integer id) {
		bookingService.cancelBooking(id);
		return new ResponseEntity<>(BaseResponse.OK(), HttpStatus.OK);
	}

	@GetMapping("/closeBooking/{id}")
	public ResponseEntity<?> closeBooking(@PathVariable("id") Integer id) {
		bookingService.closeBooking(id);
		return new ResponseEntity<>(BaseResponse.OK(), HttpStatus.OK);
	}

	@GetMapping("/availableBooking")
	public ResponseEntity<?> availableBooking() {
		int response = bookingService.getAvailablePlaceCount();
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		BookingResponse response = bookingService.getById(id);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.RECORD_NOT_FOUND), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

}
