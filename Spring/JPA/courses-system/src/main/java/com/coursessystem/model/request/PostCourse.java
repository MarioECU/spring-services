package com.coursessystem.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCourse {
	@NotEmpty
	private String title;
	@NotEmpty
	private String description;
	@NotNull
	private Long idCreator;
}
