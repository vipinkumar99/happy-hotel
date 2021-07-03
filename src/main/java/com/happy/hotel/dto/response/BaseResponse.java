package com.happy.hotel.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class BaseResponse<T> {
	private String msg;
	private T data;
	private boolean error;

	public BaseResponse(T data) {
		this("success", data, false);
	}

	public static BaseResponse<?> error(String msg) {
		return new BaseResponse<>(msg, null, true);
	}

	public static BaseResponse<?> OK() {
		return new BaseResponse<>("success", null, false);
	}
}
