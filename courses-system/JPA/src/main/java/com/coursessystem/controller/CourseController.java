package com.coursessystem.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coursessystem.model.Course;
import com.coursessystem.model.request.PostCourse;
import com.coursessystem.model.request.SubscribeRequest;
import com.coursessystem.model.response.ResponseCreation;
import com.coursessystem.model.response.ResponseGeneral;
import com.coursessystem.model.response.ResponseUpdate;
import com.coursessystem.service.CourseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "courses")
@Api(value = "Courses Management")
@AllArgsConstructor
public class CourseController {

	private final String MSG_CREATION_SUCCESS = "Se ha creado el curso con éxito";
	private final String MSG_SUBSCRIPTION_SUCCESS = "Se ha suscrito el usuario con ID %d al curso con ID %d";
	private final String MSG_UPDATE_SUCCESS = "Se ha actualizado el curso con éxito";
	private final String MSG_DELETION_SUCCESS = "Se ha eliminado el curso con ID %d";
	private final CourseService courseService;

    @GetMapping(path = "{id}")
	@ApiOperation(value = "getCourseById", notes = "Obtener curso por ID", response = Course.class)
    public ResponseEntity<Course> get(@PathVariable Long id) {
    	try {
            Course appUser = courseService.get(id);
            return ResponseEntity.ok(appUser);
    	} catch (EntityNotFoundException enf) {
            return ResponseEntity.notFound().build();
		}
    }

    @GetMapping
	@ApiOperation(value = "getCourses", notes = "Obtener todos los cursos", response = Course.class)
    public ResponseEntity<List<Course>> getAll() {
        return new ResponseEntity<>(courseService.getAll(), HttpStatus.OK);
    }

    @PostMapping
	@ApiOperation(value = "createCourse", notes = "Crear curso", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> register(@RequestBody @Valid PostCourse course, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		try {
			Course userCreated = courseService.create(course);
	        return new ResponseEntity<>(new ResponseCreation(HttpStatus.CREATED, MSG_CREATION_SUCCESS, userCreated.getId()), HttpStatus.CREATED);
		} catch (EntityNotFoundException enf) {
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.NOT_FOUND, enf.getMessage()), HttpStatus.NOT_FOUND);
		}
    }

    @PostMapping(path = "subscribe")
	@ApiOperation(value = "subscribeCourse", notes = "Subscribir un usuario en un curso", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> register(@RequestBody @Valid SubscribeRequest request, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		try {
			courseService.subscribe(request.getUserId(), request.getCourseId());
		} catch (EntityNotFoundException enf) {
			return new ResponseEntity<>(new ResponseGeneral(HttpStatus.NOT_FOUND, enf.getMessage()), HttpStatus.NOT_FOUND);
		} catch (IllegalStateException ise) {
			return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, ise.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ResponseGeneral(HttpStatus.OK, String.format(MSG_SUBSCRIPTION_SUCCESS, request.getUserId(), request.getCourseId())), HttpStatus.OK);
    }

    @PutMapping
	@ApiOperation(value = "updateCourse", notes = "Actualizar curso", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> update(@RequestBody @Valid Course user, BindingResult result) {
		if (result.hasErrors())
            return new ResponseEntity<>(new ResponseGeneral(HttpStatus.BAD_REQUEST, result.getFieldError().getField() + " " + result.getFieldError().getDefaultMessage()), HttpStatus.BAD_REQUEST);
		try {
			Course updateResult = courseService.update(user);
	        return new ResponseEntity<>(new ResponseUpdate<Course>(HttpStatus.OK, MSG_UPDATE_SUCCESS, updateResult), HttpStatus.OK);
		} catch (EntityNotFoundException enf) {
			return new ResponseEntity<>(new ResponseGeneral(HttpStatus.NOT_FOUND, enf.getMessage()), HttpStatus.NOT_FOUND);
		}
    }

    @DeleteMapping(path = "{id}")
	@ApiOperation(value = "deleteCourseById", notes = "Eliminar curso por ID", response = ResponseGeneral.class)
    public ResponseEntity<ResponseGeneral> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.ok(new ResponseGeneral(HttpStatus.OK, String.format(MSG_DELETION_SUCCESS, id)));
    }
}
