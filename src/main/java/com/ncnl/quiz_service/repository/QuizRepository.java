package com.ncnl.quiz_service.repository;

import com.ncnl.quiz_service.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    Quiz findByTitle(String title);

}
