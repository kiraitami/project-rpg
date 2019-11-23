package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.apprpg.R;

public class AddXpDialog extends Dialog {

    private Context context;
    private EditText input_xp;
    private Button add, subtract;

    public AddXpDialog(Context context){
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_xp);
        input_xp = findViewById(R.id.dialog_add_xp_value);
        add = findViewById(R.id.dialog_btn_xp_add);
        subtract = findViewById(R.id.dialog_btn_xp_subtract);
        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    public String getInputtedXp(){
        return input_xp.getText().toString();
    }

    public AddXpDialog addListener(View.OnClickListener addListener){
        add.setOnClickListener(addListener);
        return this;
    }

    public AddXpDialog subtractListener(View.OnClickListener subtractListener){
        subtract.setOnClickListener(subtractListener);
        return this;
    }
}
