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

import java.util.Collections;
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

    
    @QueryMapping
    public List<Question> getAllQuestionsByTitle(@Argument String title){

        return  quizRepository.findByTitle(title).getQuestions();
    }
    @MutationMapping
    public Question updateQuestion(@Argument String question, @Argument("input") QuestionInput input) {
        Question found = questionRepository.findByQuestion(question);
                ;

        if (input.title() != null) found.setQuestion(input.title());
        if (input.option1() != null) found.setOption1(input.option1());
        if (input.option2() != null) found.setOption2(input.option2());
        if (input.option3() != null) found.setOption3(input.option3());
        if (input.option4() != null) found.setOption4(input.option4());
        if (input.correctOption() != null) found.setCorrectAnswer(input.correctOption());

        if (input.quizId() != null) {
            Quiz quiz = quizRepository.findById(input.quizId())
                    .orElseThrow(() -> new RuntimeException("Quiz not found"));
            found.setQuiz(quiz);
        }

        return questionRepository.save(found);
    }


    @MutationMapping
    public Question addQuestion(@Argument QuestionInput questionInput, @Argument String title){
        var quiz = quizRepository.findByTitle(title);
        var question = new Question();
        question.setQuestion(questionInput.title());
        question.setOption1(questionInput.option1());
        question.setOption2(questionInput.option2());
        question.setOption3(questionInput.option3());
        question.setOption4(questionInput.option4());
        question.setCorrectAnswer(questionInput.correctOption());
        question.setQuiz(quiz);

        return questionRepository.save(question);
    }

    @MutationMapping
    public Boolean deleteQuestion(@Argument String question, @Argument String title) {

        Quiz quiz = quizRepository.findByTitle(title);

        if (quiz == null) {
            return false;
        }

        List<Question> questions = questionRepository.findByQuiz(quiz);

        if (questions.isEmpty()) {
            return false;
        }

        for (Question found : questions) {
            if (found.getQuestion().equals(question)) {
                questionRepository.deleteById(found.getId());
                return true;
            }
        }

        return false;
    }



}
