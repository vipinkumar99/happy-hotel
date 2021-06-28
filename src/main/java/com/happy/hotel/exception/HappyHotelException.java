package com.happy.hotel.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class HappyHotelException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	private HttpStatus status;

	public HappyHotelException() {
		super();
	}

	public HappyHotelException(String message) {
		super();
		this.message = message;
		this.status = HttpStatus.BAD_REQUEST;
	}

	public HappyHotelException(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}

}
