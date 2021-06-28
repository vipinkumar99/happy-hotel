package com.happy.hotel.converter;

import java.util.Objects;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.dto.response.CustomerResponse;
import com.happy.hotel.entity.CustomerEntity;

public class CustomerConverter extends BaseConverter {

	private CustomerConverter() {
		throw new UnsupportedOperationException(Msg.CLASS_CAN_NOT_INSTANTIATED);
	}

	public static CustomerEntity getEntity(CustomerRequest request) {
		if (Objects.isNull(request)) {
			return null;
		}
		CustomerEntity response = new CustomerEntity();
		response.setName(request.getName());
		response.setMobileNo(request.getMobileNo());
		response.setAge(request.getAge());
		response.setAddress(request.getAddress());
		return response;
	}

	public static CustomerResponse getResponse(CustomerEntity request) {
		if (Objects.isNull(request)) {
			return null;
		}
		CustomerResponse response = new CustomerResponse();
		response.setName(request.getName());
		response.setMobileNo(request.getMobileNo());
		response.setAge(request.getAge());
		response.setAddress(request.getAddress());
		response.setCustomerId(request.getCustomerId());
		setResponse(response, request);
		return response;
	}

	public static void setUpdate(CustomerEntity response, CustomerRequest request) {
		if (Objects.nonNull(response) && Objects.nonNull(request)) {
			response.setName(request.getName());
			response.setMobileNo(request.getMobileNo());
			response.setAge(request.getAge());
			response.setAddress(request.getAddress());
		}
	}

}
