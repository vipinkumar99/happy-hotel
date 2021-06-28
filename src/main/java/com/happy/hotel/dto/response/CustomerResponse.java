package com.happy.hotel.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponse extends BaseResponsePojo {
	private String customerId;
	private String name;
	private Integer age;
	private String address;
	private String mobileNo;
}
