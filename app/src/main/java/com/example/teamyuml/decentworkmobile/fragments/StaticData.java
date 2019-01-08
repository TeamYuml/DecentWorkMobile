package com.example.teamyuml.decentworkmobile.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.database.DBHelper;
import com.example.teamyuml.decentworkmobile.model.Cities;
import com.example.teamyuml.decentworkmobile.model.Professions;

import java.io.IOException;

public class StaticData extends Fragment {
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.cities_profiles, container, false);

        dbHelper = new DBHelper(getContext());

        try {
            dbHelper.createDataBase();
        } catch(IOException ioe) {
            throw new Error("Unable to create database");
        }

        dbHelper.openDataBase();

        Spinner professions = v.findViewById(R.id.professions);
        ArrayAdapter<Professions> adapter = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_spinner_dropdown_item, dbHelper.getProfessions());
        professions.setAdapter(adapter);

        Spinner cities = v.findViewById(R.id.cities);
        ArrayAdapter<Cities> adapter2 = new ArrayAdapter<>(getActivity(),
            android.R.layout.simple_spinner_dropdown_item, dbHelper.getCities());
        cities.setAdapter(adapter2);

        return v;
    }
}
