package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AttributesContract;
import com.example.apprpg.models.CharacterAttribute;
import com.example.apprpg.utils.StringHelper;

public class AttributesPresenter
        implements AttributesContract.AttributesPresenter {


    AttributesContract.AttributesView view;

    public AttributesPresenter(AttributesContract.AttributesView view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }

    @Override
    public void onAddAttributeClick(String name, String value) {
        if (name == null || name.isEmpty() || StringHelper.formatToName(name).isEmpty()
                || value == null || value.isEmpty()){
            view.onAddAttributeError();

        }
        else {
            view.onAddAttributeSuccessful();
            view.addAttribute( new CharacterAttribute(StringHelper.formatToName(name), Integer.parseInt(value)) );
        }
    }

    @Override
    public void onAttributeClick(int position) {
        view.onAttributeClick(position);
    }

    @Override
    public CharacterAttribute updateAttribute(String name, int value, CharacterAttribute attribute) {
        if (name == null || name.isEmpty()){
            view.onUpdateAttributeError();
            return attribute;
        }
        else {
            view.onUpdateAttributeSuccessful();
            return new CharacterAttribute(name, value);
        }
    }
}
