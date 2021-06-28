package com.happy.hotel.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.UserResponse;
import com.happy.hotel.entity.UserEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.UserRepository;
import com.happy.hotel.service.UserService;

@SpringBootTest
class UserServiceImplTest {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;

	@Captor
	private ArgumentCaptor<UserEntity> userCaptor;

	private static UserEntity userEntity;

	private static UserRequest userRequest;

	private static final int USER_ID = 1;

	@BeforeAll
	public static void init() {
		userEntity = new UserEntity();
		userEntity.setId(USER_ID);
		userEntity.setCreated(new Date());
		userEntity.setName("vipin");
		userEntity.setUsername("vipin123");
		userEntity.setPassword("123456");

		userRequest = new UserRequest();
		userRequest.setName("vipin");
		userRequest.setUsername("vipin123");
		userRequest.setPassword("123456");
	}

	@Test
	public void shouldReturnUserIdTest() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
		assertThat(userService.getById(USER_ID)).isNotNull();
		verify(userRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldReturnNullGetUserIdTest() {
		when(userRepository.findById(1)).thenReturn(Optional.empty());
		assertThat(userService.getById(USER_ID)).isNull();
		verify(userRepository, times(1)).findById(1);
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldReturnAllUsersTest() {
		when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));
		List<UserResponse> users = userService.getAll();
		assertThat(users).isNotEmpty();
		assertThat(users).hasSize(1);
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldReturnEmptyUsersTest() {
		when(userRepository.findAll()).thenReturn(new ArrayList<>());
		assertThat(userService.getAll()).isNull();
		verify(userRepository, times(1)).findAll();
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldDeleteUserByIdTest() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
		doNothing().when(userRepository).delete(userEntity);
		userService.deleteById(USER_ID);
		verify(userRepository, times(1)).findById(Mockito.anyInt());
		verify(userRepository, times(1)).delete(Mockito.any(UserEntity.class));
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldThrowUserNotFoundExcaptionDeleteTest() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> userService.deleteById(USER_ID));
		verify(userRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldSaveNewUserTest() {
		when(userRepository.save(Mockito.any())).thenReturn(userEntity);
		when(userRepository.findByUsername("vipin123")).thenReturn(Optional.empty());
		UserResponse user = userService.save(userRequest);
		verify(userRepository).save(userCaptor.capture());
		assertEquals("vipin123", user.getUsername());
		verify(userRepository, times(1)).findByUsername("vipin123");
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionSaveTest() {
		assertThrows(HappyHotelException.class, () -> userService.save(null));
		verifyNoInteractions(userRepository);
	}

	@Test
	public void shouldThrowUsernameExistExceptionSaveTest() {
		when(userRepository.findByUsername("vipin123")).thenReturn(Optional.of(userEntity));
		assertThrows(HappyHotelException.class, () -> userService.save(userRequest));
		verify(userRepository, times(1)).findByUsername(Mockito.anyString());
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldThrowEmptyRequestExceptionUpdateTest() {
		assertThrows(HappyHotelException.class, () -> userService.update(USER_ID, null));
		verifyNoInteractions(userRepository);
	}

	@Test
	public void shouldThrowUserNotFoundExceptionUpdateTest() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
		assertThrows(HappyHotelException.class, () -> userService.update(1, userRequest));
		verify(userRepository, times(1)).findById(Mockito.anyInt());
		verifyNoMoreInteractions(userRepository);
	}

	@Test
	public void shouldUpdateUserTest() {
		when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
		userService.update(USER_ID, userRequest);
		verify(userRepository).findById(Mockito.anyInt());
		verify(userRepository).save(Mockito.any(UserEntity.class));
	}

}
