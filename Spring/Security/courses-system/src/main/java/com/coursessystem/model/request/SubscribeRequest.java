package com.coursessystem.model.request;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubscribeRequest {
	@NotNull
	private Long userId;
	@NotNull
	private Long courseId;	
}
