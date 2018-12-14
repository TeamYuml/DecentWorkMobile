package com.example.teamyuml.decentworkmobile.volley;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.teamyuml.decentworkmobile.R;

public class ErrorHandler {

    public static void errorHandler(VolleyError error, Activity activity) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            toast(activity, activity.getResources().getString(R.string.noConn));
        } else if (error instanceof AuthFailureError) {
            toast(activity, activity.getResources().getString(R.string.noAuth));
        } else if (error instanceof ServerError) {
            toast(activity, activity.getResources().getString(R.string.serverError));
        } else if (error instanceof NetworkError) {
            networkErrorDefault(error, activity);
        } else if (error instanceof ParseError) {
            toast(activity, activity.getResources().getString(R.string.wrongJSON));
        }
    }

    private static void networkErrorDefault(VolleyError error, Activity activity) {
        if (error.networkResponse.statusCode == 400) {
            toast(activity, activity.getResources().getString(R.string.wrongData));
        } else if (error.networkResponse.statusCode == 401) {
            toast(activity, activity.getResources().getString(R.string.noAuth));
        } else {
            toast(activity, activity.getResources().getString(R.string.serverError));
        }
    }

    private static void toast(Activity activity, String text) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
    }
}
