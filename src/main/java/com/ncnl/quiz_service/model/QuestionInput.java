package com.ncnl.quiz_service.model;

public record QuestionInput(
        String title,
        String option1,
        String option2,
        String option3,
        String option4,
        String correctOption,
        Integer quizId
) { }
