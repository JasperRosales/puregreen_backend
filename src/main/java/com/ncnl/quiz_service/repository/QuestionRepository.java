package com.ncnl.quiz_service.repository;

import com.ncnl.quiz_service.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    Arrays findAllByQuizIdIn(List<Integer> quizId);

    List<Question> findByQuizIdIn(List<Integer> quizId);
}

