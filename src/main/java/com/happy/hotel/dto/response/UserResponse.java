package com.happy.hotel.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends BaseResponsePojo {
	private String name;
	private String username;
}
