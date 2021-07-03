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

import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.CustomerResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerControllerIT {

	private static final String INSERT_SQL = " insert into customers (id, created, address, age, customer_id, mobile_no, name) values (1, now(), 'Badshahpur', 26, 'HH-1', '9999452545', 'vipin')";
	private static final String DELETE_SQL = "delete from customers";

	@LocalServerPort
	private Integer port;

	private String url = "http://localhost:";

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		url = url.concat(port.toString()).concat("/customers");
		System.out.println("====Booking Integration test running====");
	}

	@Test
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldSaveNewCustomerTest() {
		CustomerRequest request = new CustomerRequest();
		request.setName("vipin");
		request.setAge(10);
		request.setMobileNo("9999452545");
		request.setAddress("Badshahpur");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CustomerRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.POST,
				httpRequest, new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertNotNull(response.getBody().getData());
	}

	@Test
	public void shouldReturnRequestEmptyTest() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CustomerRequest> httpRequest = new HttpEntity<>(null, headers);
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.POST,
				httpRequest, new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldCustomerGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotNull();
	}

	@Test
	public void shouldReturnNotFoundGetByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Record not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldReturnAllUsersTest() {
		ResponseEntity<BaseResponse<List<CustomerResponse>>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<List<CustomerResponse>>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
		assertThat(response.getBody().getData()).isNotEmpty();
	}

	@Test
	public void shouldReturnAllEmptyTest() {
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
				new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("No record found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void shouldDeleteByIdTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(false, response.getBody().isError());
		assertEquals("success", response.getBody().getMsg());
	}

	@Test
	public void shouldDeleteByIdNotFoundTest() {
		url = url + "/1";
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.DELETE, null,
				new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Customer not found!", response.getBody().getMsg());
	}

	@Test
	@Sql(statements = INSERT_SQL, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = DELETE_SQL, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void shouldUpdateTest() {
		url = url + "/1";
		CustomerRequest request = new CustomerRequest();
		request.setName("vipin kumar");
		request.setAge(10);
		request.setMobileNo("9999452545");
		request.setAddress("Badshahpur");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CustomerRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.PUT,
				httpRequest, new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
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
		CustomerRequest request = new CustomerRequest();
		request.setName("vipin kumar");
		request.setAge(10);
		request.setMobileNo("9999452545");
		request.setAddress("Badshahpur");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CustomerRequest> httpRequest = new HttpEntity<>(request, headers);
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.PUT,
				httpRequest, new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Customer not found!", response.getBody().getMsg());
	}

	@Test
	public void shouldReturnEmptyRequestUpdateTest() {
		url = url + "/1";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CustomerRequest> httpRequest = new HttpEntity<>(null, headers);
		ResponseEntity<BaseResponse<CustomerResponse>> response = restTemplate.exchange(url, HttpMethod.PUT,
				httpRequest, new ParameterizedTypeReference<BaseResponse<CustomerResponse>>() {
				});
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(true, response.getBody().isError());
		assertEquals("Empty request!", response.getBody().getMsg());
	}

}
