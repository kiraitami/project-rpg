package com.example.apprpg.interfaces;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.Comment;
import com.example.apprpg.models.User;

import java.util.List;

public interface PostDetailsContract {

    interface PostDetailsView {
        void getIntentData();
        void showPostData();
        void loadCommentsFromFirebase();
        void showLikersDialog();
        void commentAlertDialog();
        void visitDialog(String hostCharacterId, String hostCharacterName);
        void showCommentsList(List<Comment> commentList);
        void onCommentSuccessful();
        void onCommentEmptyField();
        void onCantComment();
    }

    interface PostDetailsPresenter {
        void validateComment(String strComment, User user, Character character, String postId);
        void requestComment(boolean canComment);
        void destroyView();
    }
}
