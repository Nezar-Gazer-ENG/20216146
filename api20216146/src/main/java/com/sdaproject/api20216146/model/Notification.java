package com.sdaproject.api20216146.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;

@Entity
@Table(name = "notifications") 
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(nullable = false) 
    private Long userId; 

    @Column(nullable = false, length = 500) 
    private String message;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private boolean sent;

    public Notification() {
    }

    public Notification(Long userId, String message, NotificationType type, boolean sent) {
        this.userId = userId;
        this.message = message;
        this.type = type;
        this.sent = sent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}