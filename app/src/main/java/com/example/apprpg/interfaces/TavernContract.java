package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Post;

import java.util.List;

public interface TavernContract {

    interface TavernView extends BaseViewContract, RecyclerFragmentViewContract {
        void requestPost();
        void loadPostsFromFirebase();
        void loadCharacterPostAmountFromFirebase();
        void showAllPosts(List<Post> postList);
        void onPostClick(Post post);
        void onPostPermissionAccepted();
        void onPostLimit();
        void onPostPermissionDenied();
    }

    interface TavernPresenter {
        void verifyPostPermissions(boolean canPost, int postAmount);
        void onDestroyView();
        void onPostClick(Post post);
    }

}
