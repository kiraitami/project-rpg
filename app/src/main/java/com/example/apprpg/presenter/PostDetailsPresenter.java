package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.PostDetailsContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Comment;
import com.example.apprpg.models.User;


public class PostDetailsPresenter
        implements PostDetailsContract.PostDetailsPresenter {

    private PostDetailsContract.PostDetailsView view;

    public PostDetailsPresenter(PostDetailsContract.PostDetailsView view) {
        this.view = view;
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
            view.onCommentSuccessful();
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
