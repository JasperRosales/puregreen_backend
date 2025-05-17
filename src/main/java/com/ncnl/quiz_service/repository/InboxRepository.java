package com.ncnl.quiz_service.repository;

import com.ncnl.quiz_service.model.AdminInbox;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InboxRepository extends JpaRepository<AdminInbox, Integer> {

   AdminInbox findBySubject(String subject);
}
