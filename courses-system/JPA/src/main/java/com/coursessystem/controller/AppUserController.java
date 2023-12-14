package com.coursessystem.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.coursessystem.model.AppUser;
import com.coursessystem.model.request.PostAppUser;
import com.coursessystem.model.response.ResponseCreation;
import com.coursessystem.model.response.ResponseGeneral;
import com.coursessystem.model.response.ResponseUpdate;
import com.coursessystem.service.AppUserService;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "users")
@Api(value = "AppUser Management")
@AllArgsConstructor
public class AppUserController {

	private final String MSG_CREATION_SUCCESS = "Se ha creado el usuario con éxito";
	private final String MSG_UPDATE_SUCCESS = "Se ha actualizado el usuario con éxito";
	private final String MSG_DELETION_SUCCESS = "Se ha eliminado el usuario con ID %d";
    private final AppUserService appUserService;

    @GetMapping(path = "{id}")
	@ApiOperation(value = "getAppUserById", notes = "Obtener usuario por ID", response = AppUser.class)
    public ResponseEntity<AppUser> get(@PathVariable Long id) {
    	try {
            AppUser appUser = appUserService.get(id);
            return ResponseEntity.ok(appUser);
    	} catch (IllegalStateException ise) {
            return ResponseEntity.notFound().build();
		}
    }

    @GetMapping
	@ApiOperation(value = "getAppUsers", notes = "Obtener todos los usuarios", response = AppUser.class)
    public ResponseEntity<List<AppUser>> getAll() {
        return new ResponseEntity<>(appUserService.getAll(), HttpStatus.OK);
    }

    @PostMapping
	@ApiOperation(value = "createAppUser", notes = "Crear usuario", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> register(@RequestBody @Valid PostAppUser user, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		try {
			AppUser userCreated = appUserService.create(user);
	        return new ResponseEntity<>(new ResponseCreation(HttpStatus.CREATED, MSG_CREATION_SUCCESS, userCreated.getId()), HttpStatus.CREATED);
		} catch (IllegalStateException ise) {
	        return new ResponseEntity<>(new ResponseGeneral(HttpStatus.CONFLICT, ise.getMessage()), HttpStatus.CONFLICT);
		}
    }

    @PutMapping
	@ApiOperation(value = "updateAppUser", notes = "Actualizar usuario", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> update(@RequestBody @Valid AppUser user, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		try {
			AppUser updateResult = appUserService.update(user);
	        return new ResponseEntity<>(new ResponseUpdate<AppUser>(HttpStatus.OK, MSG_UPDATE_SUCCESS, updateResult), HttpStatus.OK);
		} catch (EntityNotFoundException ise) {
	        return new ResponseEntity<>(new ResponseGeneral(HttpStatus.NOT_FOUND, ise.getMessage()), HttpStatus.NOT_FOUND);
		}
    }

    @DeleteMapping(path = "{id}")
	@ApiOperation(value = "deleteAppUserById", notes = "Eliminar usuario por ID", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> delete(@PathVariable Long id) {
        appUserService.delete(id);
        return ResponseEntity.ok(new ResponseGeneral(HttpStatus.OK, String.format(MSG_DELETION_SUCCESS, id)));
    }
}
