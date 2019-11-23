package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseAddInventoryObjectContract;
import com.example.apprpg.models.Maho;

public interface AddMahoContract {
    interface AddMahoView extends BaseAddInventoryObjectContract.View {

    }
    interface AddMahoPresenter extends BaseAddInventoryObjectContract.Presenter{
        void requestNewMaho(String name, String description, String damage, String difficulty,
                             String cost, String characterId, Maho maho);
    }
}
