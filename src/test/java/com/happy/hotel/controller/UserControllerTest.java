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
import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.UserResponse;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	private String url = "/users";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userService;
	@Autowired
	private ObjectMapper objectMapper;

	private static UserResponse response;
	private static UserRequest request;

	private static final int USER_ID = 1;

	@BeforeAll
	public static void init() {
		response = new UserResponse();
		response.setId(1);
		response.setCreated(new Date());
		response.setName("vipin");
		response.setUsername("vipin123");

		request = new UserRequest();
		request.setName("vipin");
		request.setUsername("vipin123");
		request.setPassword("123456");
	}

	@Test
	public void shouldNewUserSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(userService.save(Mockito.any(UserRequest.class))).thenReturn(response);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("$.data.id").value(1))
				.andExpect(jsonPath("$.data.username").value("vipin123"));
	}

	@Test
	public void shouldThrowEmptyRequestMsgSaveTest() throws Exception {
		String json = "";
		when(userService.save(null)).thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnNullSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(userService.save(Mockito.any(UserRequest.class))).thenReturn(null);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isInternalServerError()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldThrowUserAlreadyExistSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(userService.save(Mockito.any(UserRequest.class)))
				.thenThrow(new HappyHotelException(Msg.USERNAME_ALREADY_EXIST));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Username is already exist!"));
	}

	@Test
	public void shouldReturnRecordGetByIdTest() throws Exception {
		when(userService.getById(USER_ID)).thenReturn(response);
		url = url + "/" + USER_ID;
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.id").value(USER_ID));
	}

	@Test
	public void shouldReturnNoRecordFoundGetByIdTest() throws Exception {
		when(userService.getById(USER_ID)).thenReturn(null);
		url = url + "/" + USER_ID;
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Record not found!"));

	}

	@Test
	public void shouldReturnAllUserTest() throws Exception {
		when(userService.getAll()).thenReturn(Arrays.asList(response));
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data.length()").value(CoreMatchers.is(1)))
				.andExpect(jsonPath("$.data[0].id").value(USER_ID));
	}

	@Test
	public void shouldReturnRecordNotFoundAllTest() throws Exception {
		when(userService.getAll()).thenReturn(new ArrayList<>());
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("No record found!"));

	}

	@Test
	public void shouldDeleteUserByIdTest() throws Exception {
		doNothing().when(userService).deleteById(USER_ID);
		url = url + "/" + USER_ID;
		mockMvc.perform(delete(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success"));

	}

	@Test
	public void shouldReturnUserNotFoundDeleteUserByIdTest() throws Exception {
		doThrow(new HappyHotelException(Msg.USER_NOT_FOUND)).when(userService).deleteById(USER_ID);
		url = url + "/" + USER_ID;
		mockMvc.perform(delete(url)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("User not found!"));
	}

	@Test
	public void shouldUpdateTheUserTest() throws Exception {
		String json = "";
		url = url + "/" + USER_ID;
		when(userService.update(Mockito.anyInt(), Mockito.any(UserRequest.class)))
				.thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnUserNotFoundTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + USER_ID;
		when(userService.update(Mockito.anyInt(), Mockito.any(UserRequest.class)))
				.thenThrow(new HappyHotelException(Msg.USER_NOT_FOUND));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("User not found!"));
	}

	@Test
	public void shouldReturnNullUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + USER_ID;
		when(userService.update(USER_ID, null)).thenReturn(null);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldReturnResponseUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + USER_ID;
		when(userService.update(Mockito.anyInt(), Mockito.any(UserRequest.class))).thenReturn(response);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(false)).andDo(print())
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.name").value("vipin"));
	}
}
