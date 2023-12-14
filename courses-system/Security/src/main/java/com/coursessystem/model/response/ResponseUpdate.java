package com.coursessystem.model.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseUpdate<T> extends ResponseGeneral {

	private T result;

	public ResponseUpdate(HttpStatus status, String message, T result) {
		super(status, message);
		this.result = result;
	}
}
