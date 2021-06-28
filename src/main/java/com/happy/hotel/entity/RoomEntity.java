package com.happy.hotel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.happy.hotel.enums.RoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rooms")
public class RoomEntity extends BaseEntity {
	@Column(unique = true)
	private Integer capacity;
	@Enumerated(EnumType.STRING)
	private RoomStatus status;
}
