package com.example.jahadulrakib.servicehut.Notification;

public class NotificationService {
    private String notificationId;
    private String senderId;
    private String receiverId;
    private String message;
    private String proverName;

    public NotificationService() {

    }

    public NotificationService(String notificationId, String senderId, String receiverId, String message, String proverName) {
        this.notificationId = notificationId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.proverName = proverName;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProverName() {
        return proverName;
    }

    public void setProverName(String proverName) {
        this.proverName = proverName;
    }
}
