package com.alien.security.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alien.security.model.QuestionWrapper;
import com.alien.security.model.QuizResponse;
import com.alien.security.service.QuizService;

import java.util.HashMap;
import java.util.List;
// controller for quiz
@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("/create")
    public ResponseEntity<String> createQuiz(@RequestParam String category, @RequestParam int numQ, @RequestParam String title){
        return quizService.createQuiz(category, numQ, title);
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id){
        return quizService.getQuizQuestions(id);
    }

    @PostMapping("/submit")
    public ResponseEntity<HashMap<String, Integer>> submitQuiz(@RequestParam int studid,@RequestParam Long assigid ,@RequestParam Integer quizid, @RequestBody List<QuizResponse> quizResponses){
        return quizService.calculateResult(quizid , studid, assigid, quizResponses);
    }


}
