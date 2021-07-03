package com.happy.hotel.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.dto.response.CustomerResponse;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.service.CustomerService;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

	private String url = "/customers";

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CustomerService customerService;
	@Autowired
	private ObjectMapper objectMapper;

	private static CustomerResponse response;
	private static CustomerRequest request;

	private static final int CUSTOMER_ID = 1;

	@BeforeAll
	public static void init() {
		response = new CustomerResponse();
		response.setId(1);
		response.setCreated(new Date());
		response.setCustomerId("HH-1");
		response.setAge(26);
		response.setName("vipin");
		response.setAddress("Badshahpur");
		response.setMobileNo("9999452345");

		request = new CustomerRequest();
		request.setName("vipin");
		request.setAge(26);
		request.setAddress("Badshahpur");
		request.setMobileNo("9999452345");
	}

	@Test
	public void shouldNewCustomerSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(customerService.save(Mockito.any(CustomerRequest.class))).thenReturn(response);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("$.data.id").value(1));
	}

	@Test
	public void shouldThrowEmptyRequestMsgSaveTest() throws Exception {
		String json = "";
		when(customerService.save(null)).thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnNullSaveTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		when(customerService.save(Mockito.any(CustomerRequest.class))).thenReturn(null);
		mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isInternalServerError()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldReturnRecordGetByIdTest() throws Exception {
		when(customerService.getById(CUSTOMER_ID)).thenReturn(response);
		url = url + "/" + CUSTOMER_ID;
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data.id").value(CUSTOMER_ID));
	}

	@Test
	public void shouldReturnNoRecordFoundGetByIdTest() throws Exception {
		when(customerService.getById(CUSTOMER_ID)).thenReturn(null);
		url = url + "/" + CUSTOMER_ID;
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Record not found!"));

	}

	@Test
	public void shouldReturnAllCustomerTest() throws Exception {
		when(customerService.getAll()).thenReturn(Arrays.asList(response));
		mockMvc.perform(get(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success")).andExpect(jsonPath("$.data").isNotEmpty())
				.andExpect(jsonPath("$.data").isArray())
				.andExpect(jsonPath("$.data.length()").value(CoreMatchers.is(1)))
				.andExpect(jsonPath("$.data[0].id").value(CUSTOMER_ID));
	}

	@Test
	public void shouldReturnRecordNotFoundAllTest() throws Exception {
		when(customerService.getAll()).thenReturn(new ArrayList<>());
		mockMvc.perform(get(url)).andExpect(status().isNotFound()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("No record found!"));

	}

	@Test
	public void shouldDeleteCustomerByIdTest() throws Exception {
		doNothing().when(customerService).deleteById(CUSTOMER_ID);
		url = url + "/" + CUSTOMER_ID;
		mockMvc.perform(delete(url)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value(false))
				.andExpect(jsonPath("$.msg").value("success"));

	}

	@Test
	public void shouldReturnCustomerNotFoundDeleteCustomerByIdTest() throws Exception {
		doThrow(new HappyHotelException(Msg.CUSTOMER_NOT_FOUND)).when(customerService).deleteById(CUSTOMER_ID);
		url = url + "/" + CUSTOMER_ID;
		mockMvc.perform(delete(url)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Customer not found!"));
	}

	@Test
	public void shouldUpdateTheCustomerTest() throws Exception {
		String json = "";
		url = url + "/" + CUSTOMER_ID;
		when(customerService.update(Mockito.anyInt(), Mockito.any(CustomerRequest.class)))
				.thenThrow(new HappyHotelException(Msg.EMPTY_REQUEST));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value(true))
				.andExpect(jsonPath("$.msg").value("Empty request!"));
	}

	@Test
	public void shouldReturnCustomerNotFoundTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + CUSTOMER_ID;
		when(customerService.update(Mockito.anyInt(), Mockito.any(CustomerRequest.class)))
				.thenThrow(new HappyHotelException(Msg.CUSTOMER_NOT_FOUND));
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("Customer not found!"));
	}

	@Test
	public void shouldReturnNullUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + CUSTOMER_ID;
		when(customerService.update(CUSTOMER_ID, null)).thenReturn(null);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(true)).andExpect(jsonPath("$.msg").value("Something went wrong!"));
	}

	@Test
	public void shouldReturnResponseUpdateTest() throws Exception {
		String json = objectMapper.writeValueAsString(request);
		url = url + "/" + CUSTOMER_ID;
		when(customerService.update(Mockito.anyInt(), Mockito.any(CustomerRequest.class))).thenReturn(response);
		mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(jsonPath("$.error").value(false)).andExpect(jsonPath("$.msg").value("success"))
				.andExpect(jsonPath("$.data").isNotEmpty()).andExpect(jsonPath("$.data.name").value("vipin"));
	}

}
