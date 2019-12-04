package com.example.apprpg.models;

import com.example.apprpg.interfaces.base.FirebaseObjectContract;
import com.example.apprpg.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.example.apprpg.utils.StringNodes.NODE_CHARACTER;
import static com.example.apprpg.utils.StringNodes.PROVISIONAL_CONST;

public class Character
        implements Serializable, FirebaseObjectContract {

    private String id;
    private String userId;
    private String name;
    private String breed;
    private String classe;
    private String bioDescription;

    private int level;
    private int currentXP;
    private int totalXP;
    private int hp;
    private int currentHp;
    private int mp;
    private int currentMp;
    private int armor;
    private int magicResist;
    private int constitution;

    private boolean canPost;

    private List<CharacterAttribute> attributeList;

    private String profilePictureUrl;
    private String coverPictureUrl;


    public Character() { //empty constructor for firebase
    }


    @Override
    public void saveInFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

        databaseReference
                .child(NODE_CHARACTER)
                .child(this.userId)
                .child(generateId())
                .setValue(this);
    }

    @Override
    public void deleteFromFirebase() {
        DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();

        databaseReference
                .child(NODE_CHARACTER)
                .child(this.userId)
                .child(this.id)
                .removeValue();
    }

    @Override
    public String generateId() {
        if (id == null || id.isEmpty()){

            configureInitialsAttributes();

            DatabaseReference databaseReference = FirebaseHelper.getFirebaseRef();
            this.id = databaseReference
                    .child(NODE_CHARACTER)
                    .child(this.userId)
                    .push()
                    .getKey();


        }
        return this.id;
    }

    private void configureInitialsAttributes(){
        this.level = 1;
        this.hp = 10;
        this.currentHp = 10;
        this.mp = 10;
        this.currentMp = 10;
        this.constitution = 4;
        this.armor = 2;
        this.magicResist = 2;
        this.attributeList = new ArrayList<>();
        //Add Constitution as default attribute due Damage Calculator prerequisite
        this.attributeList.add(0, new CharacterAttribute(PROVISIONAL_CONST, this.constitution) );
        this.canPost = true;
    }
    
    public void recoverHp(int recoverValue){
        this.currentHp = this.currentHp + recoverValue > this.hp ? this.hp : this.currentHp + recoverValue;
    }

    public void recoverMp(int recoverValue){
        this.currentMp = this.currentMp + recoverValue > this.mp ? this.mp : this.currentMp + recoverValue;
    }

    public void receiveDamage(int damage){
        this.currentHp = this.currentHp - damage < 0 ? 0 : this.currentHp - damage;
    }

    public void spentMana(int mana){
        this.currentMp = this.currentMp - mana < 0 ? 0 : this.currentMp - mana;
    }

    public int getDamageTaken(){
        return this.hp - this.currentHp;
    }
    
    public int getManaSpent(){
        return this.mp - this.currentMp;
    }

    public String getBioDescription() {
        return bioDescription;
    }

    public void setBioDescription(String bioDescription) {
        this.bioDescription = bioDescription;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(int currentMp) {
        this.currentMp = currentMp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getCanPost() {
        return canPost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getCoverPictureUrl() {
        return coverPictureUrl;
    }

    public void setCoverPictureUrl(String coverPictureUrl) {
        this.coverPictureUrl = coverPictureUrl;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public void setCurrentXP(int currentXP) {
        this.currentXP = currentXP;
    }

    public int getTotalXP() {
        return totalXP;
    }

    public void setTotalXP(int totalXP) {
        this.totalXP = totalXP;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMagicResist() {
        return magicResist;
    }

    public void setMagicResist(int magicResist) {
        this.magicResist = magicResist;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public List<CharacterAttribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<CharacterAttribute> attributeList) {
        this.attributeList = attributeList;
    }
}
