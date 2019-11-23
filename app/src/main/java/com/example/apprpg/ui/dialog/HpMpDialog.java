package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apprpg.utils.AttributeType;
import com.example.apprpg.R;

public class HpMpDialog extends Dialog {

    private Context context;
    private TextView title;
    private EditText input_value;
    private Button add, subtract;

    public HpMpDialog(Context context, AttributeType attributeType){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hp_mp);

        title = findViewById(R.id.dialog_hp_mp_title);
        input_value = findViewById(R.id.dialog_hp_mp_input_value);
        add = findViewById(R.id.dialog_hp_mp_positive);
        subtract = findViewById(R.id.dialog_hp_mp_negative);

        if (attributeType == AttributeType.HP){
            title.setText(context.getResources().getString(R.string.dialog_hp_title));
            add.setText(context.getResources().getString(R.string.dialog_hp_heal));
            subtract.setText(context.getResources().getString(R.string.dialog_hp_damage));
        }
        else {
            title.setText(context.getResources().getString(R.string.dialog_mp_title));
            add.setText(context.getResources().getString(R.string.dialog_mp_recover));
            subtract.setText(context.getResources().getString(R.string.dialog_mp_spent));
        }

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public int getInputtedValue(){
        return input_value.getText().toString().isEmpty() ? 0 : Integer.parseInt( input_value.getText().toString() );
    }

    public HpMpDialog positiveListener(View.OnClickListener addListener){
        add.setOnClickListener(addListener);
        return this;
    }

    public HpMpDialog negativeListener(View.OnClickListener subtractListener){
        subtract.setOnClickListener(subtractListener);
        return this;
    }
}