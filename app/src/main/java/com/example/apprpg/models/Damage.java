package com.example.apprpg.models;

public class Damage {

    private int damage;
    private int armor;
    private int mr;
    private int constitution;
    private int modifier;
    private boolean isPercentage;

    private Damage(int damage, int armor, int mr, int constitution, int modifier, boolean isPercentage) {
        this.damage = damage;
        this.armor = armor;
        this.mr = mr;
        this.constitution = constitution;
        this.modifier = modifier;
        this.isPercentage = isPercentage;
    }

    public static class Builder {
        private int damage;
        private int armor;
        private int mr;
        private int constitution;
        private int modifier;
        private boolean isPercentage;

        public Builder(int damage){
            this.damage = damage;
        }

        public Builder setArmor(int armor){
            this.armor = armor;
            return this;
        }

        public Builder setMagicResit(int mr){
            this.mr = mr;
            return this;
        }

        public Builder setConstitution(int constitution){
            this.constitution = constitution;
            return this;
        }

        public Builder setModifier(int modifier){
            this.modifier = modifier;
            return this;
        }

        public Builder isPercentage(boolean isPercentage){
            this.isPercentage = isPercentage;
            return this;
        }

        public Damage build(){
            return new Damage(damage, armor, mr, constitution, modifier, isPercentage);
        }
    }


    public int calculatePhysical(){
        int restrictedConst = constitution;
        if (restrictedConst > 80)
            restrictedConst = 80;

        int firstDamageLayer = (int) ((damage - armor) - (constitution /2));
        double constPercentage = (1- (double) restrictedConst/100);

        if (isPercentage) {
            double modifierCalc = (1 + ((double) (modifier) / 100));
            return  (int) ( (firstDamageLayer * constPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * constPercentage) + (modifier) );
        }
    }

    public int calculateMagical(){
        int restrictedMr = mr;
        if (restrictedMr > 60)
            restrictedMr = 60;

        int firstDamageLayer = (int) ((damage - mr) - (mr /2));
        double mrPercentage = (1- (double) restrictedMr/100);
        if (isPercentage) {
            double modifierCalc = (1 + ((double) (modifier) / 100));
            return  (int) ( (firstDamageLayer * mrPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * mrPercentage) + (modifier) );
        }
    }

    public int calculateHybrid(){
        double armorMr = (armor + mr)/2;
        int restrictedConst = constitution;
        int restrictedMr = mr;
        int restrictedConsMR;

        if (restrictedConst > 80)
            restrictedConst = 80;

        if (restrictedMr > 60)
            restrictedMr = 60;

        restrictedConsMR = (int)((restrictedConst + restrictedMr) /2);


        int firstDamageLayer = (int) ((damage - armorMr) - (constitution /4));
        double consMRPercentage = (1- (double) restrictedConsMR/100);

        if (isPercentage) {
            double modifierCalc = (1 + ((double) (modifier) / 100));

            return  (int) ( (firstDamageLayer * consMRPercentage) * modifierCalc );
        }
        else {
            return (int) ( (firstDamageLayer * consMRPercentage) + (modifier) );
        }
    }


    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMr() {
        return mr;
    }

    public void setMr(int mr) {
        this.mr = mr;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }
}
