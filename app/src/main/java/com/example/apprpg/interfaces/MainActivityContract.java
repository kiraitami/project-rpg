package com.example.apprpg.interfaces;

import androidx.fragment.app.Fragment;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.User;

public interface MainActivityContract {

    interface MainActivityView {
        void getIntentData();
        void checkNetworkConnection();
        void changeCurrentFragment(Fragment fragment);
        void updateUserAndCharacter(User user, Character character);
    }

    interface MainActivityPresenter {
        void updateFragment(String fragmentToReplace);
        void destroyView();

    }
}
