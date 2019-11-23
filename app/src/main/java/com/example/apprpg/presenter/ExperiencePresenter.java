package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.ExperienceContract;
import com.example.apprpg.models.MyCustomDate;
import com.example.apprpg.models.Xp;

public class ExperiencePresenter implements ExperienceContract.ExperiencePresenter {

    private ExperienceContract.ExperienceView view;

    public ExperiencePresenter(ExperienceContract.ExperienceView view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }

    @Override
    public void onXpClick(int position) {
        view.onXpClick(position);
    }

    @Override
    public void requestNewXp(String amount, MyCustomDate myCustomDate, int operator) {
        if (amount == null || amount.isEmpty() || Integer.parseInt(amount) == 0) {
            view.onAddXpError();
        }
        else {
            int xp = Integer.parseInt(amount) * operator;
            view.addXp(new Xp(myCustomDate, xp));
            view.onAddXpSuccessful();
        }
    }
}
