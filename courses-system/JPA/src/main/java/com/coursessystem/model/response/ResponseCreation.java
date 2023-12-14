package com.coursessystem.model.response;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCreation extends ResponseGeneral {

	private Long idGenerated;

	public ResponseCreation(HttpStatus status, String message, Long idGenerated) {
		super(status, message);
		this.idGenerated = idGenerated;
	}

}
