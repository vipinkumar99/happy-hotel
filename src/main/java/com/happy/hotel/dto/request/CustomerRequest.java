package com.happy.hotel.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest extends BaseRequest {
	private String name;
	private Integer age;
	private String address;
	private String mobileNo;
}
