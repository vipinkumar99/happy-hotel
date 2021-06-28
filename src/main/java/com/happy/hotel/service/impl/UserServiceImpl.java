package com.happy.hotel.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.converter.UserConverter;
import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.UserResponse;
import com.happy.hotel.entity.UserEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.UserRepository;
import com.happy.hotel.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserResponse save(UserRequest request) throws HappyHotelException {
		UserEntity entity = UserConverter.getEntity(request);
		if (Objects.isNull(entity)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		if (userRepository.findByUsername(entity.getUsername()).isPresent()) {
			throw new HappyHotelException(Msg.USERNAME_ALREADY_EXIST);
		}
		return UserConverter.getResponse(userRepository.save(entity));
	}

	@Override
	public UserResponse update(Integer id, UserRequest request) throws HappyHotelException {
		if (Objects.isNull(request)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		UserEntity entity = userRepository.findById(id).orElseThrow(() -> new HappyHotelException(Msg.USER_NOT_FOUND));
		UserConverter.setUpdate(entity, request);
		return UserConverter.getResponse(userRepository.save(entity));
	}

	@Override
	public UserResponse getById(Integer id) {
		return UserConverter.getResponse(userRepository.findById(id).orElse(null));
	}

	@Override
	public List<UserResponse> getAll() {
		List<UserEntity> entities = userRepository.findAll();
		if (CollectionUtils.isEmpty(entities)) {
			return null;
		}
		return entities.parallelStream().map(user -> UserConverter.getResponse(user)).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Integer id) throws HappyHotelException {
		UserEntity entity = userRepository.findById(id).orElseThrow(() -> new HappyHotelException(Msg.USER_NOT_FOUND));
		userRepository.delete(entity);
	}

}
