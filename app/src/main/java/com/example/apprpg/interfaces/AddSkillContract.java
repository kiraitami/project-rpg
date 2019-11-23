package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseAddInventoryObjectContract;
import com.example.apprpg.models.Skill;


public interface AddSkillContract {
    interface AddSkillView extends BaseAddInventoryObjectContract.View {

    }
    interface AddSpellPresenter extends BaseAddInventoryObjectContract.Presenter{
        void requestNewSkill(String name, String description, String damage,
                             String price, String cost, String cooldown,
                             String characterId, Skill skill);
    }
}
