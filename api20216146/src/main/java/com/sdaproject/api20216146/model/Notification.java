package com.sdaproject.api20216146.model;

public class Notification {
    private Long id;
    private User user;
    private String message;
    private String type; 
    private boolean sent;

    public Notification(Long id, User user, String message, String type, boolean sent) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
