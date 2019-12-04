package com.example.apprpg.interfaces;

import com.example.apprpg.interfaces.base.BasePresenterContract;
import com.example.apprpg.interfaces.base.BaseViewContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentPresenterContract;
import com.example.apprpg.interfaces.base.RecyclerFragmentViewContract;
import com.example.apprpg.models.Maho;

import java.util.List;

public interface MahosContract {
    interface MahoView extends BaseViewContract, RecyclerFragmentViewContract {
        void showItems(List<Maho> mahoList);
        void onItemClick(Maho maho);
    }
    interface MahoPresenter extends BasePresenterContract, RecyclerFragmentPresenterContract{
        void onItemClick(Maho maho);
        void loadFromFirebase(String characterId, MahosContract.MahoView view, boolean showOrdered);
    }
}
