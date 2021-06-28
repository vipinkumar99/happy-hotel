package com.happy.hotel.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest extends BaseRequest {
	private Integer userId;
	@JsonFormat(pattern = "dd-MM-yyyy", shape = Shape.STRING, timezone = "IST")
	private LocalDate dateFrom;
	@JsonFormat(pattern = "dd-MM-yyyy", shape = Shape.STRING, timezone = "IST")
	private LocalDate dateTo;
	private Integer guestCount;
	private Boolean prepaid;
	private Integer roomId;
	private Integer customerId;
}
