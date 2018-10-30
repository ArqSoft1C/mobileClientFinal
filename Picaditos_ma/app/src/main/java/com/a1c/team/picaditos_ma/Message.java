package com.a1c.team.picaditos_ma;

public class Message {

    private String sender;
    private String receiver;
    private String subject;
    private String content;

    public Message(String sender, String receiver, String subject, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
