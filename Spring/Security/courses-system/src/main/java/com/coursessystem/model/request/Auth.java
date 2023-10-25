package com.coursessystem.model.request;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Auth {
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}