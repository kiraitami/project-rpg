package com.example.apprpg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.apprpg.models.Character;
import com.example.apprpg.utils.FirebaseHelper;
import com.example.apprpg.utils.StringHelper;
import com.example.apprpg.utils.StringNodes;
import com.example.apprpg.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class EditCharacterDialog extends Dialog {

    private Context context;
    private EditText input_name, input_breed, input_classe;
    private Button positive, negative;
    private ProgressBar progressBar;
    private Character character;
    private String oldName;
    private boolean cancelable, success;

    public EditCharacterDialog(Context context, Character character){
        super(context);
        this.cancelable = true;
        this.success = false;
        this.context = context;
        this.character = character;
        this.oldName = character.getName();

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_edit_character);

        TextView title = findViewById(R.id.edit_character_dialog_title);
        input_name = findViewById(R.id.edit_character_dialog_name);
        input_breed = findViewById(R.id.edit_character_dialog_breed);
        input_classe = findViewById(R.id.edit_character_dialog_classe);
        positive = findViewById(R.id.edit_character_dialog_positive);
        negative = findViewById(R.id.edit_character_dialog_negative);
        progressBar = findViewById(R.id.edit_character_progress);

        title.setText(context.getResources().getString(R.string.dialog_edit_char_title, character.getName()));
        input_name.setHint(context.getResources().getString(R.string.dialog_edit_char_hint_name, character.getName()));
        input_breed.setHint(context.getResources().getString(R.string.dialog_edit_char_hint_breed, character.getBreed()));
        input_classe.setHint(context.getResources().getString(R.string.dialog_edit_char_hint_classe, character.getClasse()));
        input_name.setText(character.getName());
        input_breed.setText(character.getBreed());
        input_classe.setText(character.getClasse());

        setCancelable(false);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        clickEventActions();
    }

    @Override
    public void onBackPressed() {
        if (cancelable)
            dismiss();
    }

    public Character getCharacter(){
        return this.character;
    }

    public boolean getSuccess(){
        return  this.success;
    }


    private void clickEventActions(){
        positive.setOnClickListener(view -> {
            if (validateAndSetData()){
                verifyNickname();
            }
            else {
                onEmptyFields();
            }
        });

        negative.setOnClickListener(view -> dismiss());
    }

    private void setEnableFields(boolean enableFields){
        input_name.setEnabled(enableFields);
        input_breed.setEnabled(enableFields);
        input_classe.setEnabled(enableFields);
        positive.setEnabled(enableFields);
        negative.setEnabled(enableFields);
        progressBar.setVisibility(enableFields ? View.GONE : View.VISIBLE);
    }

    private void onVerifyingNickname(){
        setEnableFields(false);
        cancelable = false;
    }

    private void onEditSuccess(){
        success = true;
        dismiss();
    }

    private void onEmptyFields(){
        setEnableFields(true);
        cancelable = true;
        Toast.makeText(context, context.getResources().getString(R.string.empty_field_error), Toast.LENGTH_SHORT).show();
    }

    private void onCollisionNickname(){
        setEnableFields(true);
        cancelable = true;
        Toast.makeText(context, context.getResources().getString(R.string.collision_character_name_error), Toast.LENGTH_SHORT).show();
    }


    private boolean validateAndSetData(){
        if ( StringHelper.formatToName(input_name.getText().toString()).isEmpty()
                || StringHelper.formatToName(input_breed.getText().toString()).isEmpty()
                || StringHelper.formatToName(input_name.getText().toString()).isEmpty() ){
            return false;
        }

        this.character.setName( StringHelper.formatToName(input_name.getText().toString()) );
        this.character.setBreed( StringHelper.formatToName(input_breed.getText().toString()) );
        this.character.setClasse( StringHelper.formatToName(input_classe.getText().toString()) );
        return true;
    }

    private void verifyNickname() {
        onVerifyingNickname();

        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
        databaseReference
                .child(StringNodes.NODE_CHARACTER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isNickValid = true;

                        for (DataSnapshot userId : dataSnapshot.getChildren()){

                            for (DataSnapshot characterId : userId.getChildren()){

                                String nameToCompare = characterId.child(StringNodes.NODE_CHARACTER_NAME).getValue(String.class);

                                if ( character.getName().equals( nameToCompare ) && !character.getName().equals(oldName) ){
                                    isNickValid = false;
                                    onCollisionNickname();
                                    break;
                                }
                            }
                        }

                        if (isNickValid){
                            character.saveInFirebase();
                            onEditSuccess();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dismiss();
                    }
                });
    }
}