package com.coursessystem.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.coursessystem.model.AppUser;
import com.coursessystem.model.Course;
import com.coursessystem.model.request.PostCourse;
import com.coursessystem.repository.AppUserRepository;
import com.coursessystem.repository.CourseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CourseService {

	private final String MSG_COURSE_NOT_FOUND = "No se encontró el curso con id %d";
	private final String MSG_SUBSCRIBER_NOT_FOUND = "No se encontró el usuario con id %d";
	private final String MSG_CREATOR_NOT_FOUND = "No se encontró el usuario con id %d";
	private final String MSG_UPDATING_NON_EXISTENT = "No se encontró el curso con id %d";
	private final String MSG_CREATOR_NON_SUBSCRIBABLE = "El creador de un curso no se puede suscribir al mismo curso";
	private final AppUserRepository appUserRepository;
	private final CourseRepository courseRepository;

    public Course get(Long id) throws EntityNotFoundException {
        return courseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format(MSG_COURSE_NOT_FOUND, id)));
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course create(PostCourse course) throws EntityNotFoundException {
    	Long creatorId = course.getIdCreator();
    	AppUser a = appUserRepository.findById(creatorId).orElseThrow(() -> new EntityNotFoundException(String.format(MSG_CREATOR_NOT_FOUND, creatorId)));
    	Course newCourse = new Course(course.getTitle(), course.getDescription(), a);
    	return courseRepository.saveAndFlush(newCourse);
    }

    public Course update(Course course) throws EntityNotFoundException {
    	Long courseId = course.getId();
        Course c = courseRepository.findById(courseId).orElseThrow(() -> new EntityNotFoundException(String.format(MSG_UPDATING_NON_EXISTENT, courseId)));
        course.setCreatedBy(c.getCreatedBy());
        course.setCreatedAt(c.getCreatedAt());
    	course.setModifiedAt(LocalDateTime.now());
    	return courseRepository.saveAndFlush(course);
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }

	public void subscribe(Long userId, Long courseId) throws EntityNotFoundException, IllegalStateException {
		Optional<AppUser> u = appUserRepository.findById(userId);
		Optional<Course> c = courseRepository.findById(courseId);
		if (u.isEmpty())
			throw new EntityNotFoundException(String.format(MSG_SUBSCRIBER_NOT_FOUND, userId));
		if (c.isEmpty())
			throw new EntityNotFoundException(String.format(MSG_COURSE_NOT_FOUND, courseId));
		AppUser user = u.get();
		Course course = c.get();
		if (course.getCreatedBy().equals(user))
			throw new IllegalStateException(MSG_CREATOR_NON_SUBSCRIBABLE);
		user.getSubscriptions().add(course);
		appUserRepository.save(user);
	}
}
