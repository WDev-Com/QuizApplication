package com.alien.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alien.security.entity.StudentAssignment;
import com.alien.security.model.AssignmentResponse;
import com.alien.security.model.QuestionWrapper;
import com.alien.security.service.AssignmentService;

@RestController
@RequestMapping("/assignment")
public class AssignmentContorller {

	@Autowired
	private AssignmentService assignmentService; 
	
	@PostMapping("/asign_work")
	public ResponseEntity<String> assignQuizToStudent(@RequestParam int studentID,@RequestParam int quizID, Integer marks  ){
		StudentAssignment sAssignment = assignmentService.assignQuiz(studentID,quizID,marks);
		if(sAssignment != null) {
		return ResponseEntity.status(HttpStatus.CREATED).body("Work Assigned Sucessfully");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Work Assigning Failed");
	}
	
	@GetMapping("/stuudentTestRes")
	public ResponseEntity<List<AssignmentResponse>> findTestDetailsOfStudent(@RequestParam int stud_id) {
	    List<AssignmentResponse> res = assignmentService.findAssignmentDetailsOfStudent(stud_id);
	    return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	@GetMapping("/getQusToSolve")
	public ResponseEntity<List<QuestionWrapper>> findquestionsAcordAssi(@RequestParam Long studAMid){
		List<QuestionWrapper> questionWrappers = assignmentService.findQuestionsByStudentAssignmentId(studAMid);
		return ResponseEntity.status(HttpStatus.OK).body(questionWrappers);
	}

}
