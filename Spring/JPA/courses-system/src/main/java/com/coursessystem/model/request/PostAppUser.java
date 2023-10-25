package com.coursessystem.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.coursessystem.model.AppUserRole;
import com.coursessystem.model.AppUserStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostAppUser {
	@NotEmpty
	private String name;
	@NotEmpty
	private String surname;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
	@NotNull
	private AppUserRole role;
	@NotNull
	private AppUserStatus estado;
}
