package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentPresenterContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.InventoryItem;

import java.util.List;

public interface ItemsContract {

    interface ItemsView extends BaseViewContract, RecyclerFragmentViewContract {
        void showItems(List<InventoryItem> itemList);
        void onItemClick(InventoryItem item);
    }

    interface ItemsPresenter extends BasePresenterContract, RecyclerFragmentPresenterContract {
        void onItemClick(InventoryItem item);
        void loadFromFirebase(String characterId, ItemsContract.ItemsView view);
    }
}
