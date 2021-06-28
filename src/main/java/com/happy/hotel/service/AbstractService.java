package com.happy.hotel.service;

import java.util.List;

import com.happy.hotel.exception.HappyHotelException;


public interface AbstractService<R, S, U> {
	R save(S request) throws HappyHotelException;
	R update(Integer id, U request) throws HappyHotelException;
	R getById(Integer id);
	List<R> getAll();
	void deleteById(Integer id) throws HappyHotelException;
}
