package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apprpg.models.Character;
import com.example.apprpg.models.Damage;
import com.example.apprpg.R;

public class DamageCalculatorDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private EditText input_damage, input_armor, input_mr, input_const, input_modifier;
    private Button btn_magic, btn_hybrid, btn_physical;
    private ImageButton btn_close;
    private CheckBox checkbox_percentage;
    private TextView result;
    private Damage mDamage;

    public DamageCalculatorDialog(Context context, Character character){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_damage_calculator);
        setCancelable(false);

        result = findViewById(R.id.dmg_calc_dialog_result);
        input_damage = findViewById(R.id.dmg_calc_dialog_damage);
        input_armor = findViewById(R.id.dmg_calc_dialog_armor);
        input_mr = findViewById(R.id.dmg_calc_dialog_mr);
        input_const = findViewById(R.id.dmg_calc_dialog_const);
        input_modifier = findViewById(R.id.dmg_calc_dialog_modfier);
        btn_magic = findViewById(R.id.dmg_calc_btn_magic);
        btn_hybrid = findViewById(R.id.dmg_calc_btn_hybrid);
        btn_physical = findViewById(R.id.dmg_calc_btn_physical);
        btn_close = findViewById(R.id.dmg_calc_btn_close);
        checkbox_percentage = findViewById(R.id.dmg_calc_dialog_checkbox);

        input_armor.setText( context.getResources().getString(R.string.dialog_hint_ARMOR, character.getArmor()) );
        input_mr.setText( context.getResources().getString(R.string.dialog_hint_MR, character.getMagicResist()) );
        input_const.setText( context.getResources().getString(R.string.dialog_hint_CONST, character.getConstitution()) );

        result.setClickable(false);
        result.setOnClickListener(this);
        btn_physical.setOnClickListener(this);
        btn_hybrid.setOnClickListener(this);
        btn_magic.setOnClickListener(this);
        btn_close.setOnClickListener(this);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void onClick(View view) {
        buildDamage();
        switch (view.getId()){

            case R.id.dmg_calc_btn_physical:
                setDamageText( mDamage.calculatePhysical() );
                break;

            case R.id.dmg_calc_btn_magic:
                setDamageText( mDamage.calculateMagical() );
                break;

            case R.id.dmg_calc_btn_hybrid:
                setDamageText( mDamage.calculateHybrid() );
                break;

            case R.id.dmg_calc_btn_close:
                dismiss();
                break;
        }
    }

    public DamageCalculatorDialog setDialogResultClickListener( View.OnClickListener resultClickListener ){
        result.setOnClickListener(resultClickListener);
        return this;
    }

    public int getDamageDealt(){
        return result.getText().toString().isEmpty() ? 0 : Integer.parseInt( result.getText().toString() );
    }

    private void setDamageText(int damage){
        //todo: custom quotes depending damage
        if (damage < 0)
            damage = 0;
        result.setText( String.valueOf(damage));
        result.setClickable(true);
    }

    private void buildDamage(){
        mDamage = new Damage.Builder( getNumberFromInput(input_damage) )
                .setArmor( getNumberFromInput(input_armor) )
                .setMagicResit( getNumberFromInput(input_mr) )
                .setConstitution( getNumberFromInput(input_const) )
                .setModifier( getNumberFromInput(input_modifier) )
                .isPercentage( checkbox_percentage.isChecked() )
                .build();
    }

    private int getNumberFromInput(EditText input){
        if (input.getText() == null || input.getText().toString().trim().isEmpty()){
            return 0;
        }
        String s = input.getText().toString().replaceAll("[^0-9]","");
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }
}
