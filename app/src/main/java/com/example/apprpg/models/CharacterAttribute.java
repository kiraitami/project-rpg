package com.example.apprpg.models;

import java.io.Serializable;

public class CharacterAttribute
        implements Serializable {

    private String name;
    private int value;

    public CharacterAttribute() {
    }

    public CharacterAttribute(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void updateAttribute(CharacterAttribute attribute){
        this.name = attribute.getName();
        this.value = attribute.getValue();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
