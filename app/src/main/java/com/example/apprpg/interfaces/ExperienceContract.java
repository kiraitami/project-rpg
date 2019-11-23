package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.models.MyCustomDate;
import com.example.apprpg.models.Xp;

import java.util.List;

public interface ExperienceContract {

    interface ExperienceView extends BaseViewContract {
        void showXp(List<Xp> xpList);
        void onXpClick(int position);
        void addXp(Xp xp);
        void loadFromFirebase();
        void updateChangesInFirebase();
        void onLoadingFromFirebase();
        void onLoadingFromFirebaseSuccess();
        void onRecyclerScroll();
        void onAddXpSuccessful();
        void onAddXpError();
    }

    interface ExperiencePresenter extends BasePresenterContract {
        void onXpClick(int position);
        void requestNewXp(String amount, MyCustomDate calendar, int operator);
    }
}
