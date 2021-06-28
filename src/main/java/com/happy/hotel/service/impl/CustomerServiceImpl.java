package com.happy.hotel.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.happy.hotel.constants.Msg;
import com.happy.hotel.converter.CustomerConverter;
import com.happy.hotel.dto.request.CustomerRequest;
import com.happy.hotel.dto.response.CustomerResponse;
import com.happy.hotel.entity.CustomerEntity;
import com.happy.hotel.exception.HappyHotelException;
import com.happy.hotel.repository.CustomerRepository;
import com.happy.hotel.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public CustomerResponse save(CustomerRequest request) throws HappyHotelException {
		CustomerEntity entity = CustomerConverter.getEntity(request);
		if (Objects.isNull(entity)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		CustomerEntity customer = customerRepository.save(entity);
		customer.setCustomerId("HH-" + customer.getId());
		customerRepository.save(customer);
		return CustomerConverter.getResponse(customer);
	}

	@Override
	public CustomerResponse update(Integer id, CustomerRequest request) throws HappyHotelException {
		if (Objects.isNull(request)) {
			throw new HappyHotelException(Msg.EMPTY_REQUEST);
		}
		CustomerEntity entity = customerRepository.findById(id)
				.orElseThrow(() -> new HappyHotelException(Msg.CUSTOMER_NOT_FOUND));
		CustomerConverter.setUpdate(entity, request);
		return CustomerConverter.getResponse(customerRepository.save(entity));
	}

	@Override
	public CustomerResponse getById(Integer id) {
		return CustomerConverter.getResponse(customerRepository.findById(id).orElse(null));
	}

	@Override
	public List<CustomerResponse> getAll() {
		List<CustomerEntity> entities = customerRepository.findAll();
		if (CollectionUtils.isEmpty(entities)) {
			return null;
		}
		return entities.parallelStream().map(customer -> CustomerConverter.getResponse(customer))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Integer id) throws HappyHotelException {
		CustomerEntity entity = customerRepository.findById(id)
				.orElseThrow(() -> new HappyHotelException(Msg.CUSTOMER_NOT_FOUND));
		customerRepository.delete(entity);
	}

}
