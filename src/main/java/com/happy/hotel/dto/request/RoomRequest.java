package com.happy.hotel.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequest extends BaseRequest {
	private Integer capacity;
}
