package com.alien.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alien.security.entity.Quiz;
import com.alien.security.entity.StudentAssignment;
import com.alien.security.entity.UserModel;
import com.alien.security.model.AssignmentResponse;
import com.alien.security.model.QuestionWrapper;
import com.alien.security.repo.QuizRepo;
import com.alien.security.repo.StudAssigementRepo;
import com.alien.security.repo.UserRepo;

@Service
public class AssignmentService {

	
	@Autowired
	private StudAssigementRepo assigementRepo;
	
	@Autowired
	private QuizRepo quizRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	public StudentAssignment assignQuiz(int studentID,int quizID, Integer Marks) {	
		
		try{ 
		Quiz quiz = quizRepo.findById(quizID).get();
		UserModel userget = userRepo.findById(studentID).get();
		
		List<Quiz> quizs  = new ArrayList<>();
		List<UserModel> users = new ArrayList<>(); 
		quizs.add(quiz);
		users.add(userget);
		StudentAssignment assignment2 = new StudentAssignment();
		assignment2.setQuiz(quizs);
		assignment2.setStudent(users);
		if (Marks == null) {
			assignment2.setMarks(0);
		} else {
			assignment2.setMarks(Marks);
		}
		
		assigementRepo.save(assignment2);
		return assignment2;
			
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("Lund Ganfd"+e.toString());
		}
		return null;
		}

	public List<AssignmentResponse> findAssignmentDetailsOfStudent(int stud_id) {
		return assigementRepo.findAssignmentResponsesByStudentId(stud_id);
	}

	public List<QuestionWrapper> findQuestionsByStudentAssignmentId(Long stud_Ass_id) {
		// TODO Auto-generated method stub
		return assigementRepo.findQuestionResponsesByStudentAssignmentId(stud_Ass_id);
	}

}
