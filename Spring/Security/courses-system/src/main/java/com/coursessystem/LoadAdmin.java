package com.coursessystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.coursessystem.model.AppUserRole;
import com.coursessystem.model.AppUserStatus;
import com.coursessystem.model.request.PostAppUser;
import com.coursessystem.service.AppUserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadAdmin implements CommandLineRunner {

	private final AppUserService appUserService;

	@Override
	public void run(String... args) throws Exception {
		if (appUserService.loadUserByUsername("admin@admin.com") == null) {
			appUserService.create(new PostAppUser("ADMIN", "ADMIN", "admin@admin.com", "sudo12345678", AppUserRole.ADMIN, AppUserStatus.ACTIVO));
		}
	}

}
