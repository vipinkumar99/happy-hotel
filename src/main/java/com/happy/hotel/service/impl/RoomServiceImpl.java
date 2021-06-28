package com.happy.hotel.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.converter.RoomConverter;
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.RoomResponse;
import com.happy.hotel.entity.RoomEntity;
import com.happy.hotel.enums.RoomStatus;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.RoomRepository;
import com.happy.hotel.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Override
	public RoomResponse save(RoomRequest request) throws HappyHotelException {
		RoomEntity entity = RoomConverter.getEntity(request);
		if (Objects.isNull(entity)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		if (roomRepository.findByCapacity(entity.getCapacity()).isPresent()) {
			throw new HappyHotelException(Msg.CAPACITY_ALREADY_EXIST);
		}
		return RoomConverter.getResponse(roomRepository.save(entity));
	}

	@Override
	public RoomResponse update(Integer id, RoomRequest request) throws HappyHotelException {
		if (Objects.isNull(request)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		RoomEntity entity = roomRepository.findById(id).orElseThrow(() -> new HappyHotelException(Msg.ROOM_NOT_FOUND));
		RoomConverter.setUpdate(entity, request);
		return RoomConverter.getResponse(roomRepository.save(entity));
	}

	@Override
	public RoomResponse getById(Integer id) {
		return RoomConverter.getResponse(roomRepository.findById(id).orElse(null));
	}

	@Override
	public List<RoomResponse> getAll() {
		List<RoomEntity> entities = roomRepository.findAll();
		if (CollectionUtils.isEmpty(entities)) {
			return null;
		}
		return entities.parallelStream().map(room -> RoomConverter.getResponse(room)).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Integer id) throws HappyHotelException {
		RoomEntity entity = roomRepository.findById(id).orElseThrow(() -> new HappyHotelException(Msg.ROOM_NOT_FOUND));
		roomRepository.delete(entity);
	}

	@Override
	public Integer findAvailableRoomId(BookingRequest bookingRequest) {
		return roomRepository.findAll().parallelStream().filter(room -> RoomStatus.UNBOOKED.equals(room.getStatus()))
				.filter(room -> room.getCapacity().equals(bookingRequest.getGuestCount())).map(room -> room.getId())
				.findFirst().orElseThrow(() -> new HappyHotelException(Msg.ROOM_NOT_AVAILABLE));
	}

	@Override
	public List<RoomResponse> getAvailableRooms() {
		return roomRepository.findAll().parallelStream().filter(room -> RoomStatus.UNBOOKED.equals(room.getStatus()))
				.map(room -> RoomConverter.getResponse(room)).collect(Collectors.toList());
	}

	@Override
	public Integer getRoomCount() {
		return (int) roomRepository.findAll().parallelStream()
				.filter(room -> RoomStatus.UNBOOKED.equals(room.getStatus())).count();
	}

	@Override
	public void bookRoom(Integer roomId) {
		RoomEntity room = roomRepository.findById(roomId)
				.orElseThrow(() -> new HappyHotelException(Msg.ROOM_NOT_FOUND));
		if (RoomStatus.BOOKED.equals(room.getStatus())) {
			throw new HappyHotelException(Msg.ROOM_BOOKED);
		}
		room.setStatus(RoomStatus.BOOKED);
		roomRepository.save(room);
	}

	@Override
	public void unbookRoom(Integer roomId) {
		RoomEntity room = roomRepository.findById(roomId)
				.orElseThrow(() -> new HappyHotelException(Msg.ROOM_NOT_FOUND));
		if (RoomStatus.BOOKED.equals(room.getStatus())) {
			room.setStatus(RoomStatus.UNBOOKED);
			roomRepository.save(room);
		}
	}

}
