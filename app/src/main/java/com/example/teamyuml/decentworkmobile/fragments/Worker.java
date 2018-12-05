package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.views.CustomListView;
import com.example.teamyuml.decentworkmobile.views.CustomWorkerView;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Worker extends Fragment {
    ArrayList<HashMap<String, String>> workerAll;
    private ListView workerList;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/profiles/four/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workerAll = new ArrayList<>();
        getWorkers();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.activity_worker, container, false);
        workerList = v.findViewById(R.id.workerList);
        return v;
    }

    private void getWorkers() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET, NOTICE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        //TODO: Add ID later and try to find better way for this.
                        JSONObject profile = response.getJSONObject(i);
                        JSONObject user = profile.getJSONObject("user");
                        String name = user.getString("first_name");
                        String last_name = user.getString("last_name");
                        JSONArray professionsJson = (JSONArray) profile.get("professions");
                        List<String> professions = new ArrayList<>();
                        for (int j = 0; j < professionsJson.length(); j++) {
                            professions.add(professionsJson.getString(j));
                        }

                        HashMap<String, String> oneNotice = new HashMap<>();
                        oneNotice.put("name", name);
                        oneNotice.put("lastName", last_name);
                        oneNotice.put("city", profile.getString("city"));

                        workerAll.add(oneNotice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (workerAll != null) {
                    initNoticeList();
                    Toast.makeText(getActivity(),
                            "Pobrano " + workerAll.size() + " profili",
                            Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        "Coś poszło nie tak",
                        Toast.LENGTH_LONG).show();
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest, "noticeList");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomWorkerView adapter = new CustomWorkerView(getActivity(), workerAll);
        workerList.setAdapter(adapter);
    }
}
