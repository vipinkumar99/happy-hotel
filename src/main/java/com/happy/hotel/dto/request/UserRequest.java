package com.happy.hotel.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest extends BaseRequest {
	private String name;
	private String username;
	private String password;
}
