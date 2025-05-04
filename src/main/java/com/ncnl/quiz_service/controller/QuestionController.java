package com.ncnl.quiz_service.controller;

import com.ncnl.quiz_service.model.Question;
import com.ncnl.quiz_service.model.QuestionInput;
import com.ncnl.quiz_service.model.Quiz;
import com.ncnl.quiz_service.repository.QuestionRepository;
import com.ncnl.quiz_service.repository.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;


    @QueryMapping
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    @QueryMapping
    public Question getQuestionById(@Argument Integer id){
        var question = questionRepository.getReferenceById(id);
        log.info("found the question {}", question);
        return question;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Question updateQuestion(@Argument Integer id, @Argument("input") QuestionInput input) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (input.title() != null) question.setQuestion(input.title());
        if (input.option1() != null) question.setOption1(input.option1());
        if (input.option2() != null) question.setOption2(input.option2());
        if (input.option3() != null) question.setOption3(input.option3());
        if (input.option4() != null) question.setOption4(input.option4());
        if (input.correctOption() != null) question.setCorrectAnswer(input.correctOption());

        if (input.quizId() != null) {
            Quiz quiz = quizRepository.findById(input.quizId())
                    .orElseThrow(() -> new RuntimeException("Quiz not found"));
            question.setQuiz(quiz);
        }

        return questionRepository.save(question);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Question addQuestion(@Argument QuestionInput questionInput, @Argument Integer quizId){
        var quiz = quizRepository.findById(quizId);
        var question = new Question();
        question.setQuestion(questionInput.title());
        question.setOption1(questionInput.option1());
        question.setOption2(questionInput.option2());
        question.setOption3(questionInput.option3());
        question.setOption4(questionInput.option4());
        question.setCorrectAnswer(questionInput.correctOption());
        question.setQuiz(quiz.orElseThrow());

        log.info(quiz.get().getTitle());
        return questionRepository.save(question);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteQuestion(@Argument Integer id, @Argument Integer quizId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (question.getQuiz().getId().equals(quizId)) {
            questionRepository.deleteById(id);
            return true;
        }

        return false;
    }

}
