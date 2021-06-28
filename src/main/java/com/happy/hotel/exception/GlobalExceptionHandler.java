package com.happy.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.response.BaseResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(HappyHotelException.class)
	public ResponseEntity<?> happyHotelExceptionHandler(HappyHotelException ex) {
		log.error("HappyHotelException ===> {}", ex);
		return new ResponseEntity<>(BaseResponse.error(ex.getMessage()), ex.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> exceptionHandler(Exception ex) {
		log.error("Exception ===> {}", ex);
		return new ResponseEntity<>(BaseResponse.error(Msg.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
		log.error("HttpMessageNotReadableException ===> {}", ex);
		BaseResponse<String> response = new BaseResponse<>();
		response.setError(true);
		if (ex.getMessage().contains("Required request body is missing:")) {
			response.setMsg(Msg.EMPTY_REQUEST);
		} else {
			response.setMsg(ex.getMessage());
		}
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
