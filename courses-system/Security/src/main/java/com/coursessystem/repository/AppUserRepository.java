package com.coursessystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursessystem.model.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
