package com.example.apprpg.interfaces.base;

public interface BaseObjectDetailsInterface {
    interface View{
        void getIntentData();
        void setViewsById();
        void showData();
        void exitWithAnimation();
        void onFavoriteClick();
        void onEditClick();
        void onDeleteClick();
    }
}
