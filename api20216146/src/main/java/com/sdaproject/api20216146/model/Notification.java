package com.sdaproject.api20216146.model;

public class Notification {

    private Long id;
    private Long userId; // Storing user ID directly instead of linking to a User object
    private String message;
    private NotificationType type;
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
