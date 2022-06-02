package com.example.noteboard.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Post {
    private String title;
    private String content;
    private Timestamp editedAt;
    private Long sharingCode;
    private String postAuthor;
    private String editedBy;
    private String id;


    public Post() {}

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


    public Long getSharingCode() {
        return sharingCode;
    }

    public void setSharingCode(long sharingCode) {
        this.sharingCode = sharingCode;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public Timestamp getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(Timestamp editedAt) {
        this.editedAt = editedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
