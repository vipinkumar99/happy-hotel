package com.happy.hotel.dto.response;

import com.happy.hotel.enums.RoomStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomResponse extends BaseResponsePojo {
	private Integer capacity;
	private RoomStatus status;
}
