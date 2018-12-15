package com.example.teamyuml.decentworkmobile.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Handle user authentication to server
 */
public class UserAuth {

    /**
     * Saves authentication data to shared preferences.
     * @param activity Calling activity.
     * @param email User's email address.
     * @param token User's authentication token from server.
     */
    public static void saveAuthData(Activity activity, String email, String token, int id) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
            "com.example.decentworkmobile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("token", token);
        editor.putInt("id", id);
        editor.commit();
    }

    /**
     * Gets and returns user's email address from shared preferences file.
     * @param activity Calling activity
     * @return User's email address.
     */
    public static String getEmail(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
            "com.example.decentworkmobile", Context.MODE_PRIVATE);
        return sharedPref.getString("email", null);
    }

    /**
     * Gets and returns user's authentication token from shared preferences file.
     * @param activity Calling activity.
     * @return User's authentication token.
     */
    public static String getToken(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
            "com.example.decentworkmobile", Context.MODE_PRIVATE);
        return sharedPref.getString("token", null);
    }

    public static int getId(Activity activity) {
        SharedPreferences sharedPref = activity.getSharedPreferences(
            "com.example.decentworkmobile", Context.MODE_PRIVATE);
        return sharedPref.getInt("id", 0);
    }

    /**
     * Adds authorization header to request.
     * @param activity Calling activity
     * @return Authorization header with token.
     */
    public static Map<String, String> authorizationHeader(Activity activity) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Token " + getToken(activity));
        return header;
    }
}
