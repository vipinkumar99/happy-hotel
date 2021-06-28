package com.happy.hotel.dto.response;

import java.time.LocalDate;

import com.happy.hotel.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponse extends BaseResponsePojo {
	private Integer userId;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private Integer guestCount;
	private Boolean prepaid;
	private Integer roomId;
	private Integer customerId;
	private BookingStatus status;
}
