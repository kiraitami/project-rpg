package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentPresenterContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Skill;

import java.util.List;

public interface SkillsContract {
    interface SkillsView extends BaseViewContract, RecyclerFragmentViewContract {
        void showItems(List<Skill> skillList);
        void onItemClick(Skill skill);
    }

    interface SkillsPresenter extends BasePresenterContract, RecyclerFragmentPresenterContract {
        void onItemClick(Skill skill);
        void loadFromFirebase(String characterId, SkillsContract.SkillsView view, boolean showOrdered);
    }
}
