package com.happy.hotel.converter;

import java.util.Objects;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.UserResponse;
import com.happy.hotel.entity.UserEntity;

public class UserConverter extends BaseConverter {

	private UserConverter() {
		throw new UnsupportedOperationException(Msg.CLASS_CAN_NOT_INSTANTIATED);
	}

	public static UserEntity getEntity(UserRequest request) {
		if (Objects.isNull(request)) {
			return null;
		}
		UserEntity response = new UserEntity();
		response.setName(request.getName());
		response.setUsername(request.getUsername());
		response.setPassword(request.getPassword());
		return response;
	}

	public static UserResponse getResponse(UserEntity request) {
		if (Objects.isNull(request)) {
			return null;
		}
		UserResponse response = new UserResponse();
		response.setName(request.getName());
		response.setUsername(request.getUsername());
		setResponse(response, request);
		return response;
	}

	public static void setUpdate(UserEntity response, UserRequest request) {
		if (Objects.nonNull(response) && Objects.nonNull(request)) {
			response.setName(request.getName());
		}
	}
}
