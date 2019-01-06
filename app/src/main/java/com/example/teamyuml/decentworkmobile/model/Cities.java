package com.example.teamyuml.decentworkmobile.model;

/*
 * Model class for City
 *
 */
public class Cities {
    public int ID_City;
    public String Name_City;

    public Cities(int ID_City, String selected_city) {
        this.ID_City = ID_City;
        this.Name_City = selected_city;
    }

    public int getID_City() {
        return ID_City;
    }

    public String getName_City() {
        return Name_City;
    }

    public void setID_City(int ID_City) {
        this.ID_City = ID_City;
    }

    public void setName_City(String name_City) {
        this.Name_City = name_City;
    }
}
