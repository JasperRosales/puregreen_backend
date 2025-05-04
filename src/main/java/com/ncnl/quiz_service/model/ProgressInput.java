package com.ncnl.quiz_service.model;

public record ProgressInput(
        User user, Quiz quiz, String status
) {
}
