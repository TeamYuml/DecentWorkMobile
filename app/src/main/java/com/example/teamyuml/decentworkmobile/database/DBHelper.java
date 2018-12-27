package com.example.teamyuml.decentworkmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Class for creating database file
 */

public class DBHelper extends SQLiteOpenHelper {

    public static String db_Name = "DecentWorkMobile.db";
    public static String cities_Table = "Cities";
    public static String professions_Table = "Professions";
    public static String COL_1_CITY = "Name_City";
    public static String COL_1_PROFESSION = "Name_Profession";

    public DBHelper(Context context) {
        super(context, db_Name, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + cities_Table + " (" +
                "ID_City INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name_City TEXT)");
        db.execSQL("create table " + professions_Table + " (" +
                "ID_Profession INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name_Profession TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + cities_Table );
        db.execSQL("DROP TABLE IF EXISTS " + professions_Table );
        onCreate(db);
    }

    /*
     * Inserting datas to Cities Table
     */
    public boolean insertDataCity(String Name_City) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues city_value = new ContentValues();
        city_value.put(COL_1_CITY, Name_City);
        long resulCity = db.insert(cities_Table, null, city_value);
        if(resulCity == -1){
            return false;
        }
        else {
            return true;
        }
    }

    /*
     * Inserting datas to Profession Table
     */
    public boolean insertDataProfession(String Name_Profession){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues profession_value = new ContentValues();
        profession_value.put(COL_1_PROFESSION, Name_Profession);
        long resulProfession = db.insert(professions_Table, null, profession_value);
        if(resulProfession == -1){
            return false;
        }
        else {
            return true;
        }
    }
}
