package com.ncnl.quiz_service.model;

public record AdminInboxInput (
        String reciever,
        String sender,
        String subject,
        String content
){
}
