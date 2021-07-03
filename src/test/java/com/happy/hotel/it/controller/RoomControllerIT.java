package com.happy.hotel.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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

import com.happy.hotel.dto.request.RoomRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.RoomResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RoomControllerIT {

	private static final String DELETE_SQL = "delete from rooms";
	private static final String INSERT_SQL = "insert into rooms (id, created, capacity, status) values (1, now(), 5, 'UNBOOKED');";

	@LocalServerPort
	private Integer port;

	private String url = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		url = url.concat(port.toString()).concat("/rooms");
		System.out.println("====Booking Integration test running====");
	}

	@Test
	@Sql(statements = DELETE_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldSaveNewRoomTest() {
		RoomRequest request = new RoomRequest();
		request.setCapacity(5);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoomRequest> httpRequest = new HttpEntity<RoomRequest>(request, header);
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnAlreadyCapacityExistTest() {
		RoomRequest request = new RoomRequest();
		request.setCapacity(5);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoomRequest> httpRequest = new HttpEntity<RoomRequest>(request, header);
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("capacity is already exist!", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnEmptyRequestTest() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoomRequest> httpRequest = new HttpEntity<RoomRequest>(null, header);
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnRoomGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	public void shouldReturnNotFoundGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Record not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnAllRoomTest() {
		ResponseEntity<BaseResponse<List<RoomResponse>>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<List<RoomResponse>>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotEmpty();
	}

	@Test
	public void shouldReturnNotFoundRoomTest() {
		ResponseEntity<BaseResponse<List<RoomResponse>>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<List<RoomResponse>>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("No record found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	public void shouldDeleteRoomByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldNotFoundDeleteRoomByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Room not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldUpdateRoomTest() {
		url = url + "/1";
		RoomRequest request = new RoomRequest();
		request.setCapacity(10);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoomRequest> httpRequest = new HttpEntity<RoomRequest>(request, header);
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	public void shouldReturnNotFoundUpdateRoomTest() {
		url = url + "/1";
		RoomRequest request = new RoomRequest();
		request.setCapacity(10);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<RoomRequest> httpRequest = new HttpEntity<RoomRequest>(request, header);
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Room not found!", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnEmptyRequestUpdateRoomTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<RoomResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, null,
				new ParameterizedTypeReference<BaseResponse<RoomResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}
}
