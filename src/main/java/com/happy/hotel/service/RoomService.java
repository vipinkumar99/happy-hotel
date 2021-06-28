package com.happy.hotel.service;

import java.util.List;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.RoomResponse;

public interface RoomService extends AbstractService<RoomResponse, RoomRequest, RoomRequest> {
	Integer findAvailableRoomId(BookingRequest bookingRequest);
	List<RoomResponse> getAvailableRooms();
	Integer getRoomCount();
	void bookRoom(Integer roomId);
	void unbookRoom(Integer roomId);
}
