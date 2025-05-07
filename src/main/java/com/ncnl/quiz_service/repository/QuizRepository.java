package com.ncnl.quiz_service.repository;

import com.ncnl.quiz_service.model.Question;
import com.ncnl.quiz_service.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findByTitle(String title);


}
