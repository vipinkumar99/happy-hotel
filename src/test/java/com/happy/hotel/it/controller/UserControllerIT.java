package com.happy.hotel.it.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.UserResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserControllerIT {

	private static final String INSERT_SQL = "insert into users (id, created, name, password, username) values (1, now(), 'vipin', '123456', 'vipin123');";
	private static final String DELETE_SQL = "delete from users";

	@LocalServerPort
	private Integer port;

	private String url = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		url = url.concat(port.toString()).concat("/users");
		System.out.println("====Booking Integration test running====");
	}

	@Test
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldSaveNewUserTest() {
		UserRequest request = new UserRequest();
		request.setName("vipin");
		request.setUsername("vipin123");
		request.setPassword("123456");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertNotNull(response.getBody().getData());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnAlreadyExistTest() {
		UserRequest request = new UserRequest();
		request.setName("vipin");
		request.setUsername("vipin123");
		request.setPassword("123456");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Username is already exist!", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnRequestEmptyTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(null, headers);
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.POST, httpRequest,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	public void shouldReturnNotFoundGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Record not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(scripts = "/insert-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = "/delete-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnAllUsersTest() {
		ResponseEntity<BaseResponse<List<UserResponse>>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<List<UserResponse>>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotEmpty();
	}

	@Test
	public void shouldReturnAllEmptyTest() {
		ResponseEntity<BaseResponse<?>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<?>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("No record found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void shouldDeleteByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldDeleteByIdNotFoundTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("User not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldUpdateTest() {
		url = url + "/1";
		UserRequest request = new UserRequest();
		request.setName("vipin kumar");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
		assertEquals("vipin kumar", response.getBody().getData().getName());
	}

	@Test
	public void shouldReturnNotFoundUpdateTest() {
		url = url + "/1";
		UserRequest request = new UserRequest();
		request.setName("vipin kumar");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("User not found!", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnEmptyRequestUpdateTest() {
		url = url + "/1";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UserRequest> httpRequest = new HttpEntity<>(null, headers);
		ResponseEntity<BaseResponse<UserResponse>> response = restTemplate.exchange(url, HttpMethod.PUT, httpRequest,
				new ParameterizedTypeReference<BaseResponse<UserResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}

}
