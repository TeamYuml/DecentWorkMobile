package com.example.teamyuml.decentworkmobile.model;

public class Professions {
    public int ID_profession;
    public String Name_profession;

    public Professions (String profession) {
        this.Name_profession = profession;
    }

    public int getID_profession() {
        return ID_profession;
    }

    public String getName_profession() {
        return Name_profession;
    }

    public void setID_profession(int ID_profession) {
        this.ID_profession = ID_profession;
    }

    public void setName_profession(String name_profession) {
        this.Name_profession = name_profession;
    }

    @Override
    public String toString() {
        return this.Name_profession;
    }
}
