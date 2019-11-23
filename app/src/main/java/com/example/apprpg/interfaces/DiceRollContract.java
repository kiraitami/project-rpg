package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Dice;

import java.util.List;

public interface DiceRollContract {
    interface DiceRollView extends BaseViewContract {
        void showResult();
        void showRollHistory();
        void loadHistoryFromFirebase();
    }

    interface DiceRollPresenter extends BasePresenterContract {
        Dice buildAndRollDice(Character character, int modifier, int diceAmount, int buttonIndex);
        void resizeHistory(String characterId, List<Dice> diceList);
    }
}
