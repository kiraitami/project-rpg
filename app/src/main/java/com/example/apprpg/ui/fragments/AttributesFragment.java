package com.example.apprpg.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprpg.interfaces.AttributesContract;
import com.example.apprpg.models.Character;
import com.example.apprpg.models.CharacterAttribute;
import com.example.apprpg.models.User;
import com.example.apprpg.presenter.AttributesPresenter;
import com.example.apprpg.ui.activities.DiceRollActivity;
import com.example.apprpg.utils.AttributeType;
import com.example.apprpg.R;
import com.example.apprpg.ui.adapters.AttributeAdapter;
import com.example.apprpg.ui.dialog.AddAttributeDialog;
import com.example.apprpg.ui.dialog.DamageCalculatorDialog;
import com.example.apprpg.ui.dialog.HpMpDialog;
import com.example.apprpg.ui.dialog.UpdateAttributeDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttributesFragment extends Fragment
        implements AttributesContract.AttributesView {


    private RecyclerView recyclerView;
    private AttributesPresenter presenter;
    private View view;
    private Toolbar toolbar;
    private FloatingActionButton fab_add_attribute;
    private TextView current_hp, current_mp, armor, magic_resist;
    private ImageView btn_current_hp, btn_current_mp, btn_armor, btn_magic_resist;
    private AddAttributeDialog addAttributeDialog;
    private UpdateAttributeDialog updateAttributeDialog;
    private HpMpDialog hpMpDialog;

    private User user;
    private Character character;


    public AttributesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attributes, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter = new AttributesPresenter(this);

        getIntentData();
        configureConstitution();
        setViewsById();

        fab_add_attribute.hide();
        toolbar.setTitle(character.getName() + " • " + character.getLevel());

        clickEventActions();
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        showAttributes();
    }

    @Override
    public void onPause() {
        character.saveInFirebase();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroyView();
        presenter = null;
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attributes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.op_menu_add_attribute:
                onAddNewAttributeClick();
                break;

            case R.id.op_menu_damage_calculator:
                damageDialog();
                break;

            case R.id.op_menu_dice_roll:
                Intent intent = new Intent(getActivity(), DiceRollActivity.class);
                intent.putExtra(getResources().getString(R.string.user_object), user);
                intent.putExtra(getResources().getString(R.string.character_object), character);
                startActivityForResult(intent, 0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void damageDialog(){
        DamageCalculatorDialog dialog = new DamageCalculatorDialog(getActivity(), character);
        dialog.setDialogResultClickListener(view1 -> {
            character.receiveDamage( dialog.getDamageDealt() );
            current_hp.setText( String.valueOf(character.getCurrentHp()) );
            if (character.getCurrentHp() <= 0){
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_kill_damage), Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        })
                .show();
    }

    @Override
    public void getIntentData() {
        Bundle data = getArguments();
        user = (User) data.getSerializable(getResources().getString(R.string.user_object));
        character = (Character) data.getSerializable(getResources().getString(R.string.character_object));
    }

    @Override
    public void setViewsById() {
        recyclerView = view.findViewById(R.id.recycler_attributes);
        toolbar = getActivity().findViewById(R.id.toolbar_main_activity);
        fab_add_attribute = getActivity().findViewById(R.id.fab_profile_main);
        current_hp = view.findViewById(R.id.tv_attr_current_hp);
        current_mp = view.findViewById(R.id.tv_attr_current_mp);
        armor = view.findViewById(R.id.tv_attr_armor);
        magic_resist = view.findViewById(R.id.tv_attr_mr);
        btn_current_hp = view.findViewById(R.id.attr_current_hp);
        btn_current_mp = view.findViewById(R.id.attr_current_mp);
        btn_armor = view.findViewById(R.id.attr_armor);
        btn_magic_resist = view.findViewById(R.id.attr_mr);
    }

    @Override
    public void clickEventActions() {
        btn_armor.setOnClickListener(view1 -> {
            updateAttributeDialog = new UpdateAttributeDialog(getActivity(), character.getArmor(), getResources().getString(R.string.armor));
            updateAttributeDialog.positiveListener(view -> {
                        character.setArmor(updateAttributeDialog.getInputtedValue());
                        armor.setText(String.valueOf(character.getArmor()));
                        updateAttributeDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        updateAttributeDialog.dismiss();
                    })
                    .show();
        });

        btn_magic_resist.setOnClickListener(view1 -> {
            updateAttributeDialog = new UpdateAttributeDialog(getActivity(), character.getMagicResist(), getResources().getString(R.string.magic_resist));
            updateAttributeDialog.positiveListener(view -> {
                        character.setMagicResist(updateAttributeDialog.getInputtedValue());
                        magic_resist.setText(String.valueOf(character.getMagicResist()));
                        updateAttributeDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        updateAttributeDialog.dismiss();
                    })
                    .show();
        });

        btn_current_hp.setOnLongClickListener(view1 -> {
            updateAttributeDialog = new UpdateAttributeDialog(getActivity(), character.getHp(), getResources().getString(R.string.hp));
            updateAttributeDialog.positiveListener(view -> {
                        int currentDamage = character.getDamageTaken();
                        character.setHp(updateAttributeDialog.getInputtedValue());
                        character.setCurrentHp( character.getHp() - currentDamage );
                        current_hp.setText(String.valueOf(character.getCurrentHp()));
                        updateAttributeDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        updateAttributeDialog.dismiss();
                    })
                    .show();
            return true;
        });

        btn_current_mp.setOnLongClickListener(view1 -> {
            updateAttributeDialog = new UpdateAttributeDialog(getActivity(), character.getMp(), getResources().getString(R.string.mp));
            updateAttributeDialog.positiveListener(view -> {
                        int currentSpent = character.getDamageTaken();
                        character.setMp(updateAttributeDialog.getInputtedValue());
                        character.setCurrentMp( character.getMp() - currentSpent );
                        current_mp.setText(String.valueOf(character.getCurrentMp()));
                        updateAttributeDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        updateAttributeDialog.dismiss();
                    })
                    .show();
            return true;
        });

        btn_current_hp.setOnClickListener(view1 -> {
            hpMpDialog = new HpMpDialog(getActivity(), AttributeType.HP);
            hpMpDialog.positiveListener(view2 -> {
                        character.recoverHp( hpMpDialog.getInputtedValue() );
                        current_hp.setText( String.valueOf( character.getCurrentHp() ) );
                        hpMpDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        character.receiveDamage( hpMpDialog.getInputtedValue() );
                        current_hp.setText( String.valueOf( character.getCurrentHp() ) );
                        hpMpDialog.dismiss();
                    })
                    .show();

        });

        btn_current_mp.setOnClickListener(view1 -> {
            hpMpDialog = new HpMpDialog(getActivity(), AttributeType.MP);
            hpMpDialog.positiveListener(view2 -> {
                        character.recoverMp( hpMpDialog.getInputtedValue() );
                        current_mp.setText( String.valueOf( character.getCurrentMp() ) );
                        hpMpDialog.dismiss();
                    })
                    .negativeListener(view2 -> {
                        character.spentMana( hpMpDialog.getInputtedValue() );
                        current_mp.setText( String.valueOf( character.getCurrentMp() ) );
                        hpMpDialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public void onAttributeClick(int position) {
        updateAttributeDialog = new UpdateAttributeDialog(getActivity(),
                character.getAttributeList().get(position),position == 0);

        updateAttributeDialog.positiveListener(view -> character.getAttributeList().set(position, presenter.updateAttribute(
                updateAttributeDialog.getInputtedName(),
                updateAttributeDialog.getInputtedValue(),
                character.getAttributeList().get(position)
        )))
                .negativeListener(view2 -> updateAttributeDialog.dismiss())
                .deleteListener(view1 -> {
                    updateAttributeDialog.dismiss();
                    character.getAttributeList().remove(position);
                    showAttributesList(character.getAttributeList());
                })
                .show();
    }

    @Override
    public void onAddNewAttributeClick() {
        addAttributeDialog = new AddAttributeDialog(getActivity());
        addAttributeDialog.positiveListener(view -> presenter.onAddAttributeClick(
                addAttributeDialog.getInputtedName(),
                addAttributeDialog.getInputtedValue()))
                .negativeListener(view2 -> addAttributeDialog.dismiss())
                .show();
    }

    @Override
    public void configureConstitution() {
        character.getAttributeList().get(0).setName(getResources().getString(R.string.constitution));
    }

    @Override
    public void showAttributes() {
        current_hp.setText(String.valueOf(character.getCurrentHp()));
        current_mp.setText(String.valueOf(character.getCurrentMp()));
        armor.setText(String.valueOf(character.getArmor()));
        magic_resist.setText(String.valueOf(character.getMagicResist()));

        showAttributesList(character.getAttributeList());
    }

    @Override
    public void showAttributesList(List<CharacterAttribute> attributeList) {
        if (isAdded()) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new AttributeAdapter(attributeList, presenter));
        }
    }

    @Override
    public void addAttribute(CharacterAttribute attribute) {
        character.getAttributeList().add(attribute);
    }

    @Override
    public void updateCharacterAttributeList() {

    }

    @Override
    public void onAddAttributeError() {
        Toast.makeText(getActivity(), getResources().getString(R.string.add_attribute_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddAttributeSuccessful() {
        showAttributesList(character.getAttributeList());
        addAttributeDialog.dismiss();
        Toast.makeText(getActivity(), getResources().getString(R.string.add_attribute_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateAttributeError() {
        Toast.makeText(getActivity(), getResources().getString(R.string.update_attribute_error), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onUpdateAttributeSuccessful() {
        showAttributesList(character.getAttributeList());
        updateAttributeDialog.dismiss();
        Toast.makeText(getActivity(), getResources().getString(R.string.update_attribute_success), Toast.LENGTH_SHORT).show();
    }
}
