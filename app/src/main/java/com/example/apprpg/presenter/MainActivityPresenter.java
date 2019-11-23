package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.MainActivityContract;
import com.example.apprpg.ui.fragments.AttributesFragment;
import com.example.apprpg.ui.fragments.ExperienceFragment;
import com.example.apprpg.ui.fragments.ItemsFragment;
import com.example.apprpg.ui.fragments.MahoFragment;
import com.example.apprpg.ui.fragments.NotesFragment;
import com.example.apprpg.ui.fragments.SkillsFragment;
import com.example.apprpg.ui.fragments.TavernFragment;
import com.example.apprpg.ui.fragments.WeaponsFragment;

import static com.example.apprpg.utils.StringNodes.*;

public class MainActivityPresenter
        implements MainActivityContract.MainActivityPresenter {

    private MainActivityContract.MainActivityView view;

    public MainActivityPresenter(MainActivityContract.MainActivityView view) {
        this.view = view;
    }

    @Override
    public void updateFragment(String fragmentToReplace) {
        switch (fragmentToReplace){
            case FRAGMENT_ATTRIBUTES:
                view.changeCurrentFragment(new AttributesFragment());
                break;

            case FRAGMENT_XP:
                view.changeCurrentFragment(new ExperienceFragment());
                break;

            case FRAGMENT_TAVERN:
                view.changeCurrentFragment(new TavernFragment());
                break;

            case FRAGMENT_NOTES:
                view.changeCurrentFragment(new NotesFragment());
                break;

            case FRAGMENT_ITEMS:
                view.changeCurrentFragment(new ItemsFragment());
                break;

            case FRAGMENT_WEAPONS:
                view.changeCurrentFragment(new WeaponsFragment());
                break;

            case FRAGMENT_SKILLS:
                view.changeCurrentFragment(new SkillsFragment());
                break;

            case FRAGMENT_MAHO:
                view.changeCurrentFragment(new MahoFragment());
                break;

            default:
                view.changeCurrentFragment(new TavernFragment());
                break;
        }
    }

    @Override
    public void destroyView() {
        view = null;
    }
}
