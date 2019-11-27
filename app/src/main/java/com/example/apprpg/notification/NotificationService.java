package com.example.apprpg.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationService {

    @Headers({
           "Authorization:key=AAAAv1he-mw:APA91bHBZw20xYm0SI2oi9F7hV9HR16XGehCm17_YsY6dTEauQnTkmNCv0KeCdxrDKd6R5iBrtTjBLVXsT9mxnHyUI09IO3B8hUpAeB5pFPD-iVrN3ymKGzDvWe_9MbXMt3gaDjm0imt",
           "Content-Type:application/json"
    })
    @POST("send")
    Call<NotificationData>saveNotification(@Body NotificationData notificationData);
}
