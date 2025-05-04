package com.ncnl.quiz_service.controller;

import com.ncnl.quiz_service.model.Question;
import com.ncnl.quiz_service.model.Quiz;
import com.ncnl.quiz_service.model.QuizInput;
import com.ncnl.quiz_service.repository.QuestionRepository;
import com.ncnl.quiz_service.repository.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class QuizController {


    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @QueryMapping
    public List<Quiz> getAllQuizzes(){
        log.info("Getting all quizzes");
        return quizRepository.findAll();
    }

    @QueryMapping
    public Quiz getQuizById(@Argument Integer id){
        var found = quizRepository.findById(id).orElseThrow();
        log.info("found the quiz {}", id);
        return found;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Quiz createQuiz(@Argument QuizInput quizInput){
        Quiz existing = quizRepository.findByTitle(quizInput.title());

        if (existing != null){
            throw new IllegalArgumentException("There is already a quiz named this");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(quizInput.title());
        quiz.setCategory(quizInput.category());
        quiz.setQuestions(quizInput.questions());

        log.info("Created a quiz");
        return quizRepository.save(quiz);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteQuizById(@Argument Integer id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quizRepository.delete(quiz);

        return true;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Quiz updateQuiz(@Argument Integer id, @Argument QuizInput input){
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        if (input.title() != null) quiz.setTitle(input.title());
        if (input.category() != null) quiz.setCategory(input.category());
        if (input.questions() != null) quiz.setQuestions(input.questions());
        return quizRepository.save(quiz);
    }


    @BatchMapping
    public List<List<Question>> questions(List<Quiz> quizzes){
        log.info("Batch mapping of questions for {} quizzes", quizzes.size());

        List<Integer> quizId = quizzes.stream()
                .map(Quiz::getId)
                .toList();

        List<Question> allQuestions = questionRepository.findByQuizIdIn(quizId);

        Map<Integer, List<Question>> questionsByQuizId = allQuestions.stream()
                .collect(Collectors.groupingBy(question -> question.getQuiz().getId()));

        return quizzes.stream()
                .map(quiz -> questionsByQuizId.getOrDefault(quiz.getId(), Collections.emptyList()))
                .toList();
    }


}
