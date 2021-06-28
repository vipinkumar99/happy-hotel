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
import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.dto.response.BaseResponse;
import com.happy.hotel.dto.response.CustomerResponse;
import com.happy.hotel.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping
	public ResponseEntity<?> save(@RequestBody CustomerRequest request) {
		CustomerResponse response = customerService.save(request);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody CustomerRequest request) {
		CustomerResponse response = customerService.update(id, request);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.SOMETHING_WENT_WRONG), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") Integer id) {
		CustomerResponse response = customerService.getById(id);
		if (Objects.isNull(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.RECORD_NOT_FOUND), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> findAll() {
		List<CustomerResponse> response = customerService.getAll();
		if (CollectionUtils.isEmpty(response)) {
			return new ResponseEntity<>(BaseResponse.error(Msg.NO_RECORD_FOUND), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Integer id) {
		customerService.deleteById(id);
		return new ResponseEntity<>(BaseResponse.OK(), HttpStatus.OK);
	}
}
