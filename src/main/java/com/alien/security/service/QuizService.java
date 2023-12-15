package com.alien.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alien.security.entity.QuestionModel;
import com.alien.security.entity.Quiz;
import com.alien.security.model.QuestionWrapper;
import com.alien.security.model.QuizResponse;
import com.alien.security.repo.QuestionRepo;
import com.alien.security.repo.QuizRepo;
import com.alien.security.repo.StudAssigementRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;
    @Autowired
    private QuestionRepo questionDao;
    
    @Autowired 
    private StudAssigementRepo assigementRepo;
    
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<QuestionModel> questions = questionDao.findRandomQuestionsByCategory(category, numQ);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizRepo.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizRepo.findById(id);
        List<QuestionModel> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for(QuestionModel q : questionsFromDB){
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestion_title(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }

        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }

    public ResponseEntity<HashMap<String, Integer>> calculateResult(Integer quizID, int studID, Long assigID, List<QuizResponse> quizResponses) {
        Quiz quiz = quizRepo.findById(quizID).get();
        List<QuestionModel> questions = quiz.getQuestions();
        boolean submitted = assigementRepo.findSubmittedByAssignmentId(assigID);
        if(submitted == false) {
        int right = 0;
        int i = 0;
        for(QuizResponse quizResponse : quizResponses){
            if(quizResponse.getResponse().equals(questions.get(i).getRight_answer()))
                right++;

            i++;
        }
        assigementRepo.updateMarksAndSubmitted(right, studID, assigID);
        HashMap<String, Integer> res =new HashMap<>();
        res.put("Successfully Submitted Quiz", right);
        return new ResponseEntity<>(res, HttpStatus.OK);}
        HashMap<String, Integer> res1 =new HashMap<>();
        res1.put("Quiz is Already Submitted ",-1);
        return new ResponseEntity<>(res1, HttpStatus.OK);
    }
}
