package com.ncnl.quiz_service.model;


import java.util.List;

public record UserInput(
        String srcode, String fullName, String password, String role, List<Progress> progress
) {}
