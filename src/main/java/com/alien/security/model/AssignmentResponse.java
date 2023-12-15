package com.alien.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssignmentResponse {

	private int student_id;
	private int quiz_id;
	private String title;
	private Long assignment_id;
	private int marks;
	
}
