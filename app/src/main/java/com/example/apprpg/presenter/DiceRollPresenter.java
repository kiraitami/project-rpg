package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.DiceRollContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.Dice;

import java.util.List;


public class DiceRollPresenter implements DiceRollContract.DiceRollPresenter {

    private DiceRollContract.DiceRollView view;

    public DiceRollPresenter(DiceRollContract.DiceRollView view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }

    @Override
    public Dice buildAndRollDice(Character character, int modifier, int diceAmount, int buttonIndex) {
        int numberOfFaces;
        switch (buttonIndex){
            case 0:
                numberOfFaces = 4;
                break;
            case 1:
                numberOfFaces = 6;
                break;
            case 2:
                numberOfFaces = 8;
                break;
            case 3:
                numberOfFaces = 12;
                break;
            case 4:
                numberOfFaces = 20;
                break;
            default:
                numberOfFaces = 0;
                break;
        }


        Dice dice = new Dice();
        dice.setCharacterName(character.getName());
        dice.setDiceAmount(diceAmount > 10 ? 10 : diceAmount);
        dice.setNumberOfFaces(numberOfFaces);
        dice.setRollModifier(modifier);
        dice.roll(character.getId());

        return dice;
    }

    @Override
    public void resizeHistory(String characterId, List<Dice> diceList) {
        if (diceList != null) {
            for (int i = diceList.size(); i > 10; i--) {
                diceList.get(diceList.size() - 1).removeDiceFromFirebase(characterId);
                diceList.remove(diceList.size() - 1);
            }
        }
    }

}
