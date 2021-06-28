package com.happy.hotel.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Date;

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
import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BookingResponse;
import com.happy.hotel.enums.BookingStatus;
import com.happy.hotel.service.BookingService;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerTest {

	private String url = "/bookings";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private BookingService bookingService;
	@Autowired
	private ObjectMapper objectMapper;

	private static BookingResponse response;
	private static BookingRequest request;

	private static final int BOOKING_ID = 1;

	@BeforeAll
	public static void init() {
		response = new BookingResponse();
		response.setId(1);
		response.setCreated(new Date());
		response.setUserId(1);
		response.setDateFrom(LocalDate.of(2020, 01, 01));
		response.setDateTo(LocalDate.of(2020, 01, 05));
		response.setGuestCount(5);
		response.setPrepaid(false);
		response.setRoomId(1);
		response.setCustomerId(1);
		response.setStatus(BookingStatus.BOOKED);

		request = new BookingRequest();
		request.setUserId(1);
		request.setDateFrom(LocalDate.of(2020, 01, 01));
		request.setDateTo(LocalDate.of(2020, 01, 05));
		request.setPrepaid(false);
		request.setCustomerId(1);
		request.setRoomId(1);

	}

	@Test
	public void shouldMakeBookingTest() throws Exception {
		url = url + "/makeBooking";
		String json = objectMapper.writeValueAsString(request);
		when(bookingService.makeBooking(Mockito.any(BookingRequest.class))).thenReturn(BOOKING_ID);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("$.data").value(BOOKING_ID));
	}

	@Test
	public void shouldCheckBookingPriceTest() throws Exception {
		url = url + "/checkPrice";
		String json = objectMapper.writeValueAsString(request);
		when(bookingService.calculatePrice(Mockito.any(BookingRequest.class))).thenReturn(1000.0);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk())
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").value(1000.0));
	}

	@Test
	public void shouldCancelBookingTest() throws Exception {
		url = url + "/cancelBooking/" + BOOKING_ID;
		doNothing().when(bookingService).cancelBooking(Mockito.anyInt());
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success"));
	}

	@Test
	public void shouldCloseBookingTest() throws Exception {
		url = url + "/closeBooking/" + BOOKING_ID;
		doNothing().when(bookingService).closeBooking(Mockito.anyInt());
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success"));
	}

	@Test
	public void shouldAvailableBookingTest() throws Exception {
		when(bookingService.getAvailablePlaceCount()).thenReturn(1);
		url = url + "/availableBooking/";
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data").value(1));
	}

	@Test
	public void shouldReturnNoRecordFoundGetByIdTest() throws Exception {
		when(bookingService.getById(Mockito.anyInt())).thenReturn(null);
		url = url + "/" + BOOKING_ID;
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Record not found!"));

	}

	@Test
	public void shouldReturnBookingResponseGetByIdTest() throws Exception {
		when(bookingService.getById(BOOKING_ID)).thenReturn(response);
		url = url + "/" + BOOKING_ID;
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.id").value(BOOKING_ID));

	}

}
