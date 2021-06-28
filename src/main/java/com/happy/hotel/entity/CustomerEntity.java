package com.happy.hotel.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class CustomerEntity extends BaseEntity {
	@Column(unique = true)
	private String customerId;
	private String name;
	private Integer age;
	private String address;
	private String mobileNo;
}
