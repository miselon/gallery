package com.miselon.gallerybackend.model;

import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "registrationToken")
@Table(name = "registrationToken_table")
public class RegistrationToken {

    @Id
    private String token;
    @Column(nullable = false)
    private Long expiresAt;
    @Column(nullable = false)
    private String userId;

    public RegistrationToken(User user) {
        this.token = UUID.randomUUID().toString();
        //TODO expiration from config
        this.expiresAt = System.currentTimeMillis() + 30*60000L;
        this.userId = user.getId();
    }

    public RegistrationToken() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setOwnerId(String userId) {
        this.userId = userId;
    }
}
