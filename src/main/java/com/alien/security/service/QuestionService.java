package com.alien.security.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alien.security.entity.QuestionModel;
import com.alien.security.repo.QuestionRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionRepo questionDao;

    public ResponseEntity<List<QuestionModel>> getAllQuestions() {
        try {
        	 List<QuestionModel> optionalQuestionData = (List<QuestionModel>)  questionDao.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(optionalQuestionData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionModel>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> addQuestion(QuestionModel question) {
        questionDao.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }
    
    public ResponseEntity<String> updateQuestionById(Integer id, QuestionModel updatedQuestion) {
        try {
            Optional<QuestionModel> existingQuestionOptional = questionDao.findById(id);
            if (existingQuestionOptional.isPresent()) {
               // Save the updated question
                questionDao.save(updatedQuestion);
                return ResponseEntity.status(HttpStatus.OK).body("Question updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating question");
        }
    }

    public ResponseEntity<String> deleteQuestionById(Integer id) {
        try {
            Optional<QuestionModel> questionOptional = questionDao.findById(id);
            
            if (questionOptional.isPresent()) {
                questionDao.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body("Question deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting question");
        }
    }

}
