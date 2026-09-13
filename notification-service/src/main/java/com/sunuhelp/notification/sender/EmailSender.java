package com.sunuhelp.notification.sender;

public interface EmailSender {
    String send(String email, String subject, String content);
}
