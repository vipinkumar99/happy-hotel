package com.happy.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happy.hotel.entity.BookingEntity;

@Repository
public interface BookingReposiotry extends JpaRepository<BookingEntity, Integer> {

}
