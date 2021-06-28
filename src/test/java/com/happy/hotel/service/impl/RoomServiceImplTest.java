package com.happy.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.RoomResponse;
import com.happy.hotel.entity.RoomEntity;
import com.happy.hotel.enums.RoomStatus;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.RoomRepository;
import com.happy.hotel.service.RoomService;

@SpringBootTest
class RoomServiceImplTest {

	@Autowired
	private RoomService roomService;

	@MockBean
	private RoomRepository roomRepository;

	@Captor
	private ArgumentCaptor<RoomEntity> roomCaptor;

	private RoomEntity roomEntity;

	private static RoomRequest roomRequest;

	private static int ROOM_ID = 1;

	@BeforeAll
	public static void init() {
		roomRequest = new RoomRequest();
		roomRequest.setCapacity(5);
	}

	@BeforeEach
	public void setup() {
		roomEntity = new RoomEntity();
		roomEntity.setId(ROOM_ID);
		roomEntity.setCreated(new Date());
		roomEntity.setCapacity(5);
		roomEntity.setStatus(RoomStatus.UNBOOKED);
	}

	@Test
	public void shouldReturnRoomByIdTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		assertNotNull(roomService.getById(ROOM_ID));
		verify(roomRepository).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnNullRoomTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());
		assertNull(roomService.getById(ROOM_ID));
		verify(roomRepository).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnAllRoomTest() {
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		List<RoomResponse> rooms = roomService.getAll();
		assertThat(rooms).isNotEmpty();
		assertThat(rooms).hasSize(1);
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnEmptyRoomListTest() {
		when(roomRepository.findAll()).thenReturn(new ArrayList<>());
		assertThat(roomService.getAll()).isNull();
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldDeleteRoomByIdTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		doNothing().when(roomRepository).delete(roomEntity);
		roomService.deleteById(ROOM_ID);
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verify(roomRepository, times(1)).delete(Mockito.any(RoomEntity.class));
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomNotFoundExceptionDeleteTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> roomService.deleteById(ROOM_ID));
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomNotFoundExceptionUpdateTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> roomService.update(ROOM_ID, roomRequest));
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionUpdateTest() {
		assertThrows(HappyHotelException.class, () -> roomService.update(ROOM_ID, null));
		verifyNoInteractions(roomRepository);
	}

	@Test
	public void shouldRoomUpdateTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		when(roomRepository.save(roomEntity)).thenReturn(roomEntity);
		roomService.update(ROOM_ID, roomRequest);
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verify(roomRepository, times(1)).save(Mockito.any(RoomEntity.class));
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnAvailableRoomIdTest() {
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		assertEquals(1, roomService.findAvailableRoomId(getBookingRequest(5)));
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomIdNotAvailableTest() {
		when(roomRepository.findAll()).thenReturn(new ArrayList<>());
		assertThrows(HappyHotelException.class, () -> roomService.findAvailableRoomId(getBookingRequest(2)));
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnAvailableRoomsTest() {
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		assertThat(roomService.getAvailableRooms()).isNotEmpty();
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnEmptyAvailableRoomsTest() {
		roomEntity.setStatus(RoomStatus.BOOKED);
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		assertThat(roomService.getAvailableRooms()).isEmpty();
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnOneRoomCountTest() {
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		assertEquals(1, roomService.getRoomCount());
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldReturnZeroRoomCountTest() {
		roomEntity.setStatus(RoomStatus.BOOKED);
		when(roomRepository.findAll()).thenReturn(Arrays.asList(roomEntity));
		assertEquals(0, roomService.getRoomCount());
		verify(roomRepository, times(1)).findAll();
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomNotFoundBookRoomTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> roomService.bookRoom(1));
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomBookedExceptionTest() {
		roomEntity.setStatus(RoomStatus.BOOKED);
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		assertThrows(HappyHotelException.class, () -> roomService.bookRoom(ROOM_ID));
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldRoomBookTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		roomService.bookRoom(ROOM_ID);
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verify(roomRepository, times(1)).save(Mockito.any(RoomEntity.class));
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowRoomNotFoundExceptionUnbookRoomTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> roomService.unbookRoom(1));
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldRoomUnbookTest() {
		roomEntity.setStatus(RoomStatus.BOOKED);
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		roomService.unbookRoom(ROOM_ID);
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verify(roomRepository, times(1)).save(Mockito.any());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldRoomUnbookedUnbookAgainTest() {
		when(roomRepository.findById(ROOM_ID)).thenReturn(Optional.of(roomEntity));
		roomService.unbookRoom(ROOM_ID);
		verify(roomRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldNewRoomSaveTest() {
		when(roomRepository.findByCapacity(5)).thenReturn(Optional.empty());
		when(roomRepository.save(roomEntity)).thenReturn(roomEntity);
		roomService.save(roomRequest);
		verify(roomRepository).save(roomCaptor.capture());
		verify(roomRepository, times(1)).findByCapacity(5);
		verifyNoMoreInteractions(roomRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionSaveTest() {
		assertThrows(HappyHotelException.class, () -> roomService.save(null));
		verifyNoInteractions(roomRepository);
	}

	@Test
	public void shouldThrowCapacityExistTest() {
		when(roomRepository.findByCapacity(5)).thenReturn(Optional.of(roomEntity));
		assertThrows(HappyHotelException.class, () -> roomService.save(roomRequest));
		verify(roomRepository, times(1)).findByCapacity(Mockito.anyInt());
		verifyNoMoreInteractions(roomRepository);
	}

	public static BookingRequest getBookingRequest(int guestCount) {
		BookingRequest request = new BookingRequest();
		request.setUserId(1);
		request.setDateFrom(LocalDate.now());
		request.setDateTo(LocalDate.now().plusDays(1));
		request.setGuestCount(guestCount);
		request.setPrepaid(false);
		request.setCustomerId(1);
		request.setRoomId(1);
		return request;
	}

}
