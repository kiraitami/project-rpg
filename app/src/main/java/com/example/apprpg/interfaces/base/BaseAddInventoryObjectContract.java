package com.example.apprpg.interfaces.base;

public interface BaseAddInventoryObjectContract {

    interface View extends BaseViewContract {

        void showData();
        void onEmptyFields();
        void backToFragment();
        void exitWithAnimation();
        void setEnableFields(boolean enableFields);
        void onAddLoading();
        void onAddSuccessful();
        void onAddFailed();
        void onEditSuccessful();

    }

    interface Presenter extends BasePresenterContract {

    }
}
