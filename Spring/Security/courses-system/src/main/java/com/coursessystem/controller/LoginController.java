package com.coursessystem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.coursessystem.config.JwtUtils;
import com.coursessystem.model.request.Auth;
import com.coursessystem.model.response.ResponseGeneral;
import com.coursessystem.service.AppUserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "login")
@Api(value = "Login Management")
@AllArgsConstructor
public class LoginController {

	private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtUtils jwtUtils;

    @PostMapping
	@ApiOperation(value = "login", notes = "Iniciar sesión", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> auth(@RequestBody @Valid Auth request, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		final UserDetails user = appUserService.loadUserByUsername(request.getUsername());
		if (user != null) {
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.OK, jwtUtils.generateToken(user)), HttpStatus.OK);
		}
        return new ResponseEntity<>(new ResponseGeneral(HttpStatus.NOT_FOUND, String.format("Usuario %s no encontrado", request.getUsername())), HttpStatus.NOT_FOUND);
    }

}
