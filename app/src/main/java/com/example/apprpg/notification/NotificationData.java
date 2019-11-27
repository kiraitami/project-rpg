package com.example.apprpg.notification;

public class NotificationData {
    private String to;
    private MyNotification notification;

    public NotificationData(String to, MyNotification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MyNotification getNotification() {
        return notification;
    }

    public void setNotification(MyNotification notification) {
        this.notification = notification;
    }


}


