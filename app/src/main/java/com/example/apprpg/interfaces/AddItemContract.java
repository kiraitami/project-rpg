package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BaseAddInventoryObjectContract;
import com.example.apprpg.models.InventoryItem;

public interface AddItemContract {

    interface AddItemView extends BaseAddInventoryObjectContract.View {

    }

    interface AddItemPresenter extends BaseAddInventoryObjectContract.Presenter {

        void requestNewItem(String name, String description, String amount, String characterId, InventoryItem item);

    }
}
