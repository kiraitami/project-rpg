package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.PostDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Comment;
import com.example.apprpg.models.User;
import com.example.apprpg.notification.BaseUrlsAndTopics;
import com.example.apprpg.notification.MyNotification;
import com.example.apprpg.notification.NotificationData;
import com.example.apprpg.notification.NotificationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PostDetailsPresenter
        implements PostDetailsContract.PostDetailsPresenter {

    private PostDetailsContract.PostDetailsView view;

    public PostDetailsPresenter(PostDetailsContract.PostDetailsView view) {
        this.view = view;
    }

    @Override
    public  void buildNotification(String title, String body, String postId){

        String to = BaseUrlsAndTopics.BASE_TOPIC+postId;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrlsAndTopics.BASE_NOTIFICATION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NotificationData notificationData = new NotificationData(to,
                new MyNotification(title, body));

        NotificationService service = retrofit.create(NotificationService.class);
        Call<NotificationData> call = service.saveNotification(notificationData);
        call.enqueue(new Callback<NotificationData>() {
            @Override
            public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {
            }

            @Override
            public void onFailure(Call<NotificationData> call, Throwable t) {
            }
        });
    }

    @Override
    public void validateComment(String strComment, User user, Character character, String postId) {
        if (strComment.trim().isEmpty()){
            view.onCommentEmptyField();
        }
        else {
            Comment comment = new Comment();
            comment.generateId();
            comment.setBody(strComment);
            comment.setPostId(postId);
            comment.setCharacterId(character.getId());
            comment.setCharacterName(character.getName());
            comment.setUserId(character.getUserId());
            comment.setCharacterPictureUrl(character.getProfilePictureUrl());
            comment.setUserName(user.getNickname());
            comment.saveInFirebase();
            view.onCommentSuccessful(comment.getBody());
        }
    }

    @Override
    public void requestComment(boolean canComment) {
        if (canComment){
            view.commentAlertDialog();
        }
        else {
            view.onCantComment();
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
