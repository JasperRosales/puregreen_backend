package com.ncnl.quiz_service.model;

import java.util.List;

public record QuizInput (
        String title, String category, List<Question> questions
){
}
