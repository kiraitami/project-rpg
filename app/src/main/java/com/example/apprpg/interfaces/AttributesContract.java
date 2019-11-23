package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.models.CharacterAttribute;

import java.util.List;

public interface AttributesContract {

    interface AttributesView extends BaseViewContract {

        void configureConstitution();
        void showAttributes();
        void showAttributesList(List<CharacterAttribute> attributeList);
        void onAttributeClick(int position);
        void onAddNewAttributeClick();
        void addAttribute(CharacterAttribute attribute);
        void updateCharacterAttributeList();
        void onAddAttributeError();
        void onAddAttributeSuccessful();
        void onUpdateAttributeError();
        void onUpdateAttributeSuccessful();
    }

    interface AttributesPresenter extends BasePresenterContract {
        void onAddAttributeClick(String name, String value);
        void onAttributeClick(int position);
        CharacterAttribute updateAttribute(String name, int value, CharacterAttribute attribute);
    }
}
