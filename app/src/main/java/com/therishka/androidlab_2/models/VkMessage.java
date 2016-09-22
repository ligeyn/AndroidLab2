package com.therishka.androidlab_2.models;

/**
 * @author Rishad Mustafaev.
 */

public class VkMessage {
    private long id;
    private long date;
    private long user_id;
    private boolean isRead;
    private String title;
    private String messageBody;

    public VkMessage(long id, long date, long user_id, boolean isRead, String title, String messageBody) {
        this.id = id;
        this.date = date;
        this.user_id = user_id;
        this.isRead = isRead;
        this.title = title;
        this.messageBody = messageBody;
    }

    public VkMessage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
