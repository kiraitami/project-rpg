package com.example.apprpg.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationService {

    @Headers({
           "Authorization:key=PUT_YOUR_KEY_HERE",
           "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificationData>saveNotification(@Body NotificationData notificationData);
}
