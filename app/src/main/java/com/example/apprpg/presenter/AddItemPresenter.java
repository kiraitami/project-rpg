package com.example.apprpg.presenter;

import com.example.apprpg.interfaces.AddItemContract;
import com.example.apprpg.models.InventoryItem;

import static com.example.apprpg.utils.StringHelper.formatToName;

public class AddItemPresenter 
        implements AddItemContract.AddItemPresenter {
    
    
    private AddItemContract.AddItemView view;

    public AddItemPresenter(AddItemContract.AddItemView view) {
        this.view = view;
    }

    @Override
    public void requestNewItem(String name, String description, String amount, String characterId, InventoryItem item) {
        view.onAddLoading();
        boolean isNewItem = item == null;

        if (name.trim().isEmpty() || formatToName(name).isEmpty() || description.trim().isEmpty() || amount.trim().isEmpty()){
            view.onEmptyFields();
        }
        else {
            if (isNewItem) {
                item = new InventoryItem();
            }

            item.setCharacterId(characterId);
            item.setName(formatToName(name));
            item.setDescription(description);
            item.setAmount(Integer.parseInt(amount));
            item.setFavorite(0);
            item.saveInFirebase();

            if (isNewItem) {
                view.onAddSuccessful();
            }
            else {
                view.onEditSuccessful();
            }
        }
    }

    @Override
    public void onDestroyView() {
        this.view = null;
    }
}
