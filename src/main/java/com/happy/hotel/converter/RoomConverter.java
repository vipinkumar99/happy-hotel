package com.happy.hotel.converter;

import java.util.Objects;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.RoomResponse;
import com.happy.hotel.entity.RoomEntity;
import com.happy.hotel.enums.RoomStatus;

public class RoomConverter extends BaseConverter {

	private RoomConverter() {
		throw new UnsupportedOperationException(Msg.CLASS_CAN_NOT_INSTANTIATED);
	}

	public static RoomEntity getEntity(RoomRequest request) {
		if (Objects.isNull(request)) {
			return null;
		}
		RoomEntity response = new RoomEntity();
		response.setCapacity(request.getCapacity());
		response.setStatus(RoomStatus.UNBOOKED);
		return response;
	}

	public static RoomResponse getResponse(RoomEntity request) {
		if (Objects.isNull(request)) {
			return null;
		}
		RoomResponse response = new RoomResponse();
		response.setCapacity(request.getCapacity());
		response.setStatus(request.getStatus());
		setResponse(response, request);
		return response;
	}

	public static void setUpdate(RoomEntity response, RoomRequest request) {
		if (Objects.nonNull(response) && Objects.nonNull(request)) {
			response.setCapacity(request.getCapacity());
		}
	}

}
