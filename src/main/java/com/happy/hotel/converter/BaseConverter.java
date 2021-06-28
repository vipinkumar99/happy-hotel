package com.happy.hotel.converter;

import java.util.Objects;

import com.happy.hotel.dto.response.BaseResponsePojo;
import com.happy.hotel.entity.BaseEntity;

public class BaseConverter {

	public static void setResponse(BaseResponsePojo response, BaseEntity request) {
		if (Objects.nonNull(response) && Objects.nonNull(request)) {
			response.setId(request.getId());
			response.setCreated(request.getCreated());
		}
	}
}
