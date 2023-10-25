package com.coursessystem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.coursessystem.model.response.ResponseGeneral;

@ControllerAdvice
public class RequestExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseGeneral> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, ex.getMessage().split(";")[0]), HttpStatus.BAD_REQUEST);
	}
}
