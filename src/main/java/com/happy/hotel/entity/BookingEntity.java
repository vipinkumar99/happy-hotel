package com.happy.hotel.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.happy.hotel.enums.BookingStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class BookingEntity extends BaseEntity {
	private Integer userId;
	private LocalDate dateFrom;
	private LocalDate dateTo;
	private Integer guestCount;
	private Boolean prepaid;
	private Integer roomId;
	private Integer customerId;
	@Enumerated(EnumType.STRING)
	private BookingStatus status;
}
