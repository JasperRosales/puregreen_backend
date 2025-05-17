package com.ncnl.quiz_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_inbox")
@Data
public class AdminInbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String reciever;
    private String sender;
    private String subject;
    private String content;
    @CreationTimestamp
    private LocalDateTime created_at;
}
