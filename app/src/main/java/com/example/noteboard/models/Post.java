package com.example.noteboard.models;

import java.util.Date;

public class Post {
    private String title;
    private String content;
    private Date editedAt;
    private String sharingCode;

    public Post(String title, String content, Date editedAt, String sharingCode) {
        this.title = title;
        this.content = content;
        this.editedAt = editedAt;
        this.sharingCode = sharingCode.toString();
    }

    public Post() {

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

    public Date getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Date editedAt) {
        this.editedAt = editedAt;
    }

    public String getSharingCode() {
        return sharingCode;
    }

    public void setSharingCode(String sharingCode) {
        this.sharingCode = sharingCode;
    }
}
