package com.coursessystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coursessystem.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
