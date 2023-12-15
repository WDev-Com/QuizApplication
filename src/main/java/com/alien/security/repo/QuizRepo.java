package com.alien.security.repo;
import org.springframework.data.repository.CrudRepository;

import com.alien.security.entity.Quiz;

public interface QuizRepo extends CrudRepository<Quiz,Integer> {


}
