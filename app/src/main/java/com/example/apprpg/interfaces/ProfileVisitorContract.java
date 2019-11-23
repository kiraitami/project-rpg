package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseViewContract;

public interface ProfileVisitorContract {

    interface ProfileVisitorView extends BaseViewContract {
        void showData();
        void loadHostCharacterFromFirebase();
        void onLoadHostSuccess();
        void onLoadHostFailed();

    }
}
