package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.apprpg.R;

public class AddAttributeDialog extends Dialog {

    private Context context;
    private EditText input_name, input_value;
    private Button positive, negative;

    public AddAttributeDialog(Context context){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_attribute);
        input_name = findViewById(R.id.add_attr_dialog_name);
        input_value = findViewById(R.id.add_attr_dialog_value);
        positive = findViewById(R.id.add_attr_dialog_positive);
        negative = findViewById(R.id.add_attr_dialog_negative);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public String getInputtedName(){
        return input_name.getText().toString();
    }

    public String getInputtedValue(){
        return input_value.getText().toString();
    }

    public AddAttributeDialog positiveListener(View.OnClickListener positiveListener){
        positive.setOnClickListener(positiveListener);
        return this;
    }

    public AddAttributeDialog negativeListener(View.OnClickListener negativeListener){
        negative.setOnClickListener(negativeListener);
        return this;
    }
}
