package com.happy.hotel.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.RoomResponse;
import com.happy.hotel.enums.RoomStatus;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.service.RoomService;

@SpringBootTest
@AutoConfigureMockMvc
class RoomControllerTest {

	private String url = "/rooms";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private RoomService roomService;
	@Autowired
	private ObjectMapper objectMapper;

	private static RoomResponse response;
	private static RoomRequest request;

	private static final int ROOM_ID = 1;

	@BeforeAll
	public static void init() {
		response = new RoomResponse();
		response.setId(1);
		response.setCreated(new Date());
		response.setCapacity(5);
		response.setStatus(RoomStatus.UNBOOKED);
		request = new RoomRequest();
		request.setCapacity(5);
	}

	@Test
	public void shouldNewRoomSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(roomService.save(Mockito.any(RoomRequest.class))).thenReturn(response);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("$.data.id").value(1));
	}

	@Test
	public void shouldThrowEmptyRequestMsgSaveTest() throws Exception {
		String json = "";
		when(roomService.save(null)).thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnNullSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(roomService.save(Mockito.any(RoomRequest.class))).thenReturn(null);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isInternalServerError()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldThrowRoomCapacityExistSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(roomService.save(Mockito.any(RoomRequest.class)))
				.thenThrow(new HappyHotelException(Msg.CAPACITY_ALREADY_EXIST));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("capacity is already exist!"));
	}

	@Test
	public void shouldReturnRecordGetByIdTest() throws Exception {
		when(roomService.getById(ROOM_ID)).thenReturn(response);
		url = url + "/" + ROOM_ID;
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.id").value(ROOM_ID));
	}

	@Test
	public void shouldReturnNoRecordFoundGetByIdTest() throws Exception {
		when(roomService.getById(ROOM_ID)).thenReturn(null);
		url = url + "/" + ROOM_ID;
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Record not found!"));

	}

	@Test
	public void shouldReturnAllRoomTest() throws Exception {
		when(roomService.getAll()).thenReturn(Arrays.asList(response));
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data.length()").value(CoreMatchers.is(1)))
				.andExpect(jsonPath("$.data[0].id").value(ROOM_ID));
	}

	@Test
	public void shouldReturnRecordNotFoundAllTest() throws Exception {
		when(roomService.getAll()).thenReturn(new ArrayList<>());
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("No record found!"));

	}

	@Test
	public void shouldDeleteRoomByIdTest() throws Exception {
		doNothing().when(roomService).deleteById(ROOM_ID);
		url = url + "/" + ROOM_ID;
		mockMvc.perform(delete(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success"));

	}

	@Test
	public void shouldReturnRoomNotFoundDeleteRoomByIdTest() throws Exception {
		doThrow(new HappyHotelException(Msg.ROOM_NOT_FOUND)).when(roomService).deleteById(ROOM_ID);
		url = url + "/" + ROOM_ID;
		mockMvc.perform(delete(url)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Room not found!"));
	}

	@Test
	public void shouldUpdateTheRoomTest() throws Exception {
		String json = "";
		url = url + "/" + ROOM_ID;
		when(roomService.update(Mockito.anyInt(), Mockito.any(RoomRequest.class)))
				.thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnRoomNotFoundTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + ROOM_ID;
		when(roomService.update(Mockito.anyInt(), Mockito.any(RoomRequest.class)))
				.thenThrow(new HappyHotelException(Msg.ROOM_NOT_FOUND));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("Room not found!"));
	}

	@Test
	public void shouldReturnNullUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + ROOM_ID;
		when(roomService.update(ROOM_ID, null)).thenReturn(null);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldReturnResponseUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + ROOM_ID;
		when(roomService.update(Mockito.anyInt(), Mockito.any(RoomRequest.class))).thenReturn(response);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(false)).andDo(print())
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.capacity").value(5));
	}
}
