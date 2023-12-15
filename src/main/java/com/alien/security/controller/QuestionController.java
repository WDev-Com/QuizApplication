package com.alien.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alien.security.entity.QuestionModel;
import com.alien.security.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/allQuestions")
    public ResponseEntity<List<QuestionModel>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<QuestionModel>> getQuestionsByCategory(@PathVariable String category){
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody QuestionModel question){
        return  questionService.addQuestion(question);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateQuestionById(@PathVariable Integer id, @RequestBody QuestionModel updatedQuestion) {
        return questionService.updateQuestionById(id, updatedQuestion);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestionById(@PathVariable Integer id) {
        return questionService.deleteQuestionById(id);
    }

}