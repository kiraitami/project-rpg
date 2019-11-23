package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.TavernContract;
import com.example.apprpg.models.Post;

public class TavernPresenter
        implements TavernContract.TavernPresenter {

    private TavernContract.TavernView tavernView;

    public TavernPresenter(TavernContract.TavernView tavernView) {
        this.tavernView = tavernView;
    }

    @Override
    public void verifyPostPermissions(boolean canPost, int postAmount) {
        if (!canPost){
            tavernView.onPostPermissionDenied();
        }
        else if (postAmount >= 10){
            tavernView.onPostLimit();
        }
        else {
            tavernView.onPostPermissionAccepted();
        }
    }

    @Override
    public void onDestroyView() {
        tavernView = null;
    }

    @Override
    public void onPostClick(Post post) {
        tavernView.onPostClick(post);
    }
}
