package com.alien.security.repo;

import com.alien.security.entity.QuestionModel;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends CrudRepository<QuestionModel, Integer> {

    List<QuestionModel> findByCategory(String category);
    
    @Query(value = "SELECT * FROM quizdata q WHERE q.category = :category ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
    List<QuestionModel> findRandomQuestionsByCategory(String category, int numQ);


}
