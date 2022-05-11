package com.example.noteboard.models;

import java.sql.Timestamp;

public class Post {
    private String title;
    private String content;
    private Timestamp editedAt;
    private String sharingCode;

    public Post(String title, String content, Timestamp editedAt, String sharingCode) {
        this.title = title;
        this.content = content;
        this.editedAt = editedAt;
        this.sharingCode = sharingCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Timestamp editedAt) {
        this.editedAt = editedAt;
    }

    public String getSharingCode() {
        return sharingCode;
    }

    public void setSharingCode(String sharingCode) {
        this.sharingCode = sharingCode;
    }
}
