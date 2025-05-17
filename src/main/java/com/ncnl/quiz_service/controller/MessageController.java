package com.ncnl.quiz_service.controller;

import com.ncnl.quiz_service.model.AdminInbox;
import com.ncnl.quiz_service.model.AdminInboxInput;
import com.ncnl.quiz_service.repository.InboxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class MessageController {

    @Autowired
    private InboxRepository inboxRepository;

    @QueryMapping
    public List<AdminInbox> getAllMessage(){
        return inboxRepository.findAll();
    }

    @MutationMapping
    public AdminInbox addMessage(@Argument AdminInboxInput input){
        AdminInbox inbox = new AdminInbox();
        inbox.setReciever(input.reciever());
        inbox.setSender(input.sender());
        inbox.setSubject(input.subject());
        inbox.setContent(input.content());

        return inboxRepository.save(inbox);
    }

    @MutationMapping
    public Boolean deleteMessage(@Argument String subject){
        AdminInbox found = inboxRepository.findBySubject(subject);

        inboxRepository.deleteById(found.getId());


        return true;

    }
}
