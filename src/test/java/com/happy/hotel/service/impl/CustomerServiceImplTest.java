package com.happy.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.entity.CustomerEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.CustomerRepository;
import com.happy.hotel.service.CustomerService;

@SpringBootTest
class CustomerServiceImplTest {

	@Autowired
	private CustomerService customerService;

	@MockBean
	private CustomerRepository customerRepository;

	@Captor
	private ArgumentCaptor<CustomerEntity> customerCaptor;

	private static final int CUSTOMER_ID = 1;

	private static CustomerEntity customerEntity;

	private static CustomerRequest customerRequest;

	@BeforeAll
	public static void setup() {
		customerEntity = new CustomerEntity();
		customerEntity.setId(CUSTOMER_ID);
		customerEntity.setCreated(new Date());
		customerEntity.setCustomerId("HH-1");
		customerEntity.setName("vipin");
		customerEntity.setAge(26);
		customerEntity.setAddress("Badshahpur");
		customerEntity.setMobileNo("9999452345");

		customerRequest = new CustomerRequest();
		customerRequest.setName("vipin");
		customerRequest.setAge(26);
		customerRequest.setAddress("Badshahpur");
		customerRequest.setMobileNo("9999452345");
	}

	@Test
	public void shouldReturnCustomerByIdTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerEntity));
		assertThat(customerService.getById(CUSTOMER_ID)).isNotNull();
		verify(customerRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldReturnNullCustomerByIdTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
		assertThat(customerService.getById(1)).isNull();
		verify(customerRepository, times(1)).findById(1);
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldReturnAllCustomerTest() {
		when(customerRepository.findAll()).thenReturn(Arrays.asList(customerEntity));
		assertThat(customerService.getAll()).isNotEmpty();
		verify(customerRepository, times(1)).findAll();
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldReturnEmptyCustomerTest() {
		when(customerRepository.findAll()).thenReturn(new ArrayList<>());
		assertThat(customerService.getAll()).isNull();
		verify(customerRepository, times(1)).findAll();
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldDeleteCustomerByIdTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerEntity));
		doNothing().when(customerRepository).delete(customerEntity);
		customerService.deleteById(CUSTOMER_ID);
		verify(customerRepository, times(1)).findById(Mockito.anyInt());
		verify(customerRepository, times(1)).delete(Mockito.any(CustomerEntity.class));
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldThrowCustomerNotFoundExcaptionDeleteTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> customerService.deleteById(CUSTOMER_ID));
		verify(customerRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldSaveCustomerTest() {
		when(customerRepository.save(Mockito.any())).thenReturn(customerEntity);
		assertNotNull(customerService.save(customerRequest));
		verify(customerRepository, times(2)).save(Mockito.any());
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionSaveTest() {
		assertThrows(HappyHotelException.class, () -> customerService.save(null));
		verifyNoInteractions(customerRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionUpdateTest() {
		assertThrows(HappyHotelException.class, () -> customerService.update(CUSTOMER_ID, null));
		verifyNoInteractions(customerRepository);
	}

	@Test
	public void shouldThrowCustomerNotFoundExceptionUpdateTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> customerService.update(1, customerRequest));
		verify(customerRepository, times(1)).findById(CUSTOMER_ID);
		verifyNoMoreInteractions(customerRepository);
	}

	@Test
	public void shouldUpdateCustomerTest() {
		when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(customerEntity));
		customerService.update(CUSTOMER_ID, customerRequest);
		verify(customerRepository).findById(CUSTOMER_ID);
		verify(customerRepository).save(customerEntity);
	}

}
