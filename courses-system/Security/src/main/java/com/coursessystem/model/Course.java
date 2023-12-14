package com.coursessystem.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Course {

	@SequenceGenerator(
			name = "course_sequence",
			sequenceName = "course_sequence",
			allocationSize = 1
	)
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "course_sequence"
	)
	@NotNull
	private Long id;
	private String title;
	private String description;
	@Enumerated(EnumType.STRING)
	private CourseStatus status;
    @ManyToOne
    @JoinColumn(name="creator_id", nullable=false)
    private AppUser createdBy;
	@ApiModelProperty(hidden = true)
	private LocalDateTime createdAt;
	@ApiModelProperty(hidden = true)
	private LocalDateTime modifiedAt;

	public Course(String title, String description, AppUser creator) {
		this.title = title;
		this.description = description;
		this.status = CourseStatus.IN_PROGRESS;
		this.createdBy = creator;
		this.createdAt = LocalDateTime.now();
	}

}
