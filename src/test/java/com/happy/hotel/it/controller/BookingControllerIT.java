package com.happy.hotel.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.happy.hotel.dto.request.BookingRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.BookingResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BookingControllerIT {

	private static final String INSERT_BOOKING_SQL = "insert into bookings (id, created, customer_id, date_from, date_to, guest_count, prepaid, room_id, status, user_id) values (1, now(), 1, '2020-01-01', '2020-01-05', 5, false, 1, 'BOOKED', 1)";
	private static final String INSERT_ROOM_SQL = "insert into rooms (id, created, capacity, status) values (1, now(), 5, 'UNBOOKED')";
	private static final String DELETE_ROOM_SQL = "delete from rooms";
	private static final String DELETE_BOOKING_SQL = "delete from bookings";

	@LocalServerPort
	private Integer port;

	private String url = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		url = url.concat(port.toString()).concat("/bookings");
		System.out.println("====Booking Integration test running====");
	}

	@Test
	@Sql(statements = INSERT_ROOM_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_ROOM_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(statements = DELETE_BOOKING_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldMakeBookingTest() {
		url = url + "/makeBooking";
		BookingRequest request = new BookingRequest();
		request.setUserId(1);
		request.setGuestCount(5);
		request.setDateFrom(LocalDate.of(2020, 01, 01));
		request.setDateTo(LocalDate.of(2020, 01, 05));
		request.setPrepaid(false);
		request.setCustomerId(1);
		request.setRoomId(1);

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<BookingRequest> httpRequest = new HttpEntity<BookingRequest>(request, header);
		ResponseEntity<BaseResponse<Integer>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<Integer>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	public void shouldReturnBookingPriceTest() {
		url = url + "/checkPrice";
		BookingRequest request = new BookingRequest();
		request.setUserId(1);
		request.setGuestCount(5);
		request.setDateFrom(LocalDate.of(2020, 01, 01));
		request.setDateTo(LocalDate.of(2020, 01, 05));
		request.setPrepaid(false);
		request.setCustomerId(1);
		request.setRoomId(1);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<BookingRequest> httpRequest = new HttpEntity<BookingRequest>(request, header);
		ResponseEntity<BaseResponse<Double>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<Double>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	@Sql(statements = INSERT_BOOKING_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = INSERT_ROOM_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_ROOM_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(statements = DELETE_BOOKING_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldCancelBookingRequestTest() {
		url = url + "/cancelBooking/1";
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldCancelBookingNotFoundTest() {
		url = url + "/cancelBooking/1";
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Booking not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_BOOKING_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = INSERT_ROOM_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_ROOM_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(statements = DELETE_BOOKING_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldCloseBookingRequestTest() {
		url = url + "/closeBooking/1";
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldCloseBookingNotFoundTest() {
		url = url + "/closeBooking/1";
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Booking not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_ROOM_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_ROOM_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldAvailableBookingCountTest() {
		url = url + "/availableBooking";
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnNotFoundGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<BookingResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<BookingResponse>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Record not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_BOOKING_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_BOOKING_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnBookingGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<BookingResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<BookingResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

}
