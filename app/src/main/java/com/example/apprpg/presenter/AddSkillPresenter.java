package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AddSkillContract;
import com.example.apprpg.models.Skill;
import com.example.apprpg.utils.StringHelper;

public class AddSkillPresenter implements AddSkillContract.AddSpellPresenter {
    
    
    private AddSkillContract.AddSkillView view;


    public AddSkillPresenter(AddSkillContract.AddSkillView view) {
        this.view = view;
    }

    @Override
    public void requestNewSkill(String name, String description, String damage, String price, String cost, String cooldown, String characterId, Skill skill) {
        view.onAddLoading();
        boolean isNewSkill = skill == null;

        if (name.trim().isEmpty()  || StringHelper.removeEmojis(name).isEmpty()
                || description.trim().isEmpty() ){
            view.onEmptyFields();
        }
        else {
            if (isNewSkill) {
                skill = new Skill();
            }

            skill.setCharacterId(characterId);
            skill.setName(StringHelper.removeEmojis(name.trim()));
            skill.setDescription(description);
            skill.setDamage(damage.trim().isEmpty() ? null : StringHelper.removeEmojis(damage.trim()));
            skill.setPrice(price.trim().isEmpty() ? null : StringHelper.removeEmojis(price.trim()));
            skill.setCost(cost.trim().isEmpty() ? null : StringHelper.removeEmojis(cost.trim()));
            skill.setCooldown(cooldown.trim().isEmpty() ? null : StringHelper.removeEmojis(cooldown));
            skill.setFavorite(0);
            skill.saveInFirebase();

            if (isNewSkill) {
                view.onAddSuccessful();
            }
            else {
                view.onEditSuccessful();
            }
        }
    }

    @Override
    public void onDestroyView() {
        this.view =  null;
    }
}
