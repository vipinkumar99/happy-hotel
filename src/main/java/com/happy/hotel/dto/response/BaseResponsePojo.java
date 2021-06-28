package com.happy.hotel.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponsePojo {
	private Integer id;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", shape = Shape.STRING, timezone = "IST")
	private Date created;
}
