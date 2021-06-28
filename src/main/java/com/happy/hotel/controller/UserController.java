package com.happy.hotel.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.dto.request.UserRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.UserResponse;
import com.happy.hotel.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody UserRequest request) {
		UserResponse response = userService.save(request);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody UserRequest request) {
		UserResponse response = userService.update(id, request);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
		UserResponse response = userService.getById(id);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.RECORD_NOT_FOUND), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<UserResponse> response = userService.getAll();
		if (CollectionUtils.isEmpty(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.NO_RECORD_FOUND), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
		userService.deleteById(id);
		return new ResponseEntity<>(BaseResponse.OK(), HttpStatus.OK);
	}

}
