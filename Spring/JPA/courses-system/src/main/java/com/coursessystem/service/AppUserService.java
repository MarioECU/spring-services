package com.coursessystem.service;

import lombok.AllArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coursessystem.model.AppUser;
import com.coursessystem.model.request.PostAppUser;
import com.coursessystem.repository.AppUserRepository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final String MSG_USER_NOT_FOUND = "No se encontró el usuario con id %d";
	private final String MSG_UPDATING_NON_EXISTENT = "No se encontró el usuario con id %d";
    private final String MSG_USER_ALREADY_REGISTERED = "El usuario %s ya se encuentra registrado";
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public AppUser get(Long id) throws UsernameNotFoundException {
        return appUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(String.format(MSG_USER_NOT_FOUND, id)));
    }

    public List<AppUser> getAll() {
        return appUserRepository.findAll();
    }

    public AppUser create(PostAppUser user) throws IllegalStateException {
        if (appUserRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException(String.format(MSG_USER_ALREADY_REGISTERED, user.getUsername()));
        }
        AppUser newUser = new AppUser(user.getName(), user.getSurname(), user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole(), user.getEstado());
        return appUserRepository.saveAndFlush(newUser);
    }

    public AppUser update(AppUser user) throws EntityNotFoundException {
		appUserRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(String.format(MSG_UPDATING_NON_EXISTENT, user.getId())));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setModifiedAt(LocalDateTime.now());
		return appUserRepository.saveAndFlush(user);
    }

    public void delete(Long id) {
        appUserRepository.deleteById(id);
    }
}
