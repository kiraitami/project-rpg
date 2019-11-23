package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.apprpg.models.CharacterAttribute;
import com.example.apprpg.R;

public class UpdateAttributeDialog extends Dialog {

    private Context context;
    private TextView title;
    private EditText input_name, input_value;
    private Button positive, negative;
    private ImageButton delete;

    public UpdateAttributeDialog(Context context, CharacterAttribute attribute, boolean hideDeleteButton){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_attribute);
        input_name = findViewById(R.id.update_attr_dialog_name);
        input_value = findViewById(R.id.update_attr_dialog_value);
        positive = findViewById(R.id.update_attr_dialog_positive);
        negative = findViewById(R.id.update_attr_dialog_negative);
        delete = findViewById(R.id.update_attr_dialog_delete);
        delete.setVisibility( hideDeleteButton ? View.GONE : View.VISIBLE);
        input_name.setEnabled(!hideDeleteButton);
        input_name.setText(attribute.getName());
        input_value.setText(String.valueOf(attribute.getValue()));
        input_name.setHint(context.getResources().getString(R.string.alert_update_attr_hint_name, attribute.getName()));
        input_value.setHint(context.getResources().getString(R.string.alert_update_attr_hint_value, attribute.getValue()));
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public UpdateAttributeDialog(Context context, int value, String name){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update_attribute);
        title = findViewById(R.id.update_attr_title);
        input_name = findViewById(R.id.update_attr_dialog_name);
        input_value = findViewById(R.id.update_attr_dialog_value);
        positive = findViewById(R.id.update_attr_dialog_positive);
        negative = findViewById(R.id.update_attr_dialog_negative);
        delete = findViewById(R.id.update_attr_dialog_delete);
        delete.setVisibility(View.GONE);
        input_name.setText(name);
        input_name.setEnabled(false);
        input_value.setText(String.valueOf(value));
        input_value.setHint(context.getResources().getString(R.string.alert_update_attr_hint_value, value));
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public String getInputtedName(){
        return input_name.getText().toString();
    }

    public int  getInputtedValue(){
        return input_value.getText().toString().isEmpty() ? 0 : Integer.parseInt(input_value.getText().toString());
    }

    public UpdateAttributeDialog positiveListener(View.OnClickListener positiveListener){
        positive.setOnClickListener(positiveListener);
        return this;
    }

    public UpdateAttributeDialog negativeListener(View.OnClickListener negativeListener){
        negative.setOnClickListener(negativeListener);
        return this;
    }

    public UpdateAttributeDialog deleteListener(View.OnClickListener deleteListener){
        delete.setOnClickListener(deleteListener);
        return this;
    }

    public UpdateAttributeDialog setInputMaxLength(int maxLength){
        input_value.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        return  this;
    }

    public UpdateAttributeDialog setTitle(String strTitle){
        title.setText(strTitle);
        return  this;
    }
}
