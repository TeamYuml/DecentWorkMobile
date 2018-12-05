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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.views.CustomListView;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Worker extends Fragment {
    ArrayList<HashMap<String, String>> workerAll;
    private ListView workerList;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/profiles/four/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWorkers();
        super.onCreate(savedInstanceState);
        workerAll = new ArrayList<>();
        System.out.println("DIALA");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.notice_list_view, container, false);
        workerList = v.findViewById(R.id.noticeList);
        return v;
    }

    private void getWorkers() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, NOTICE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String name = null;
                String lastName = null;
                String profession = null;
                String city = null;
                Integer id = null;

                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject notice = results.getJSONObject(i);
                        id = notice.getInt("id");
                        name = notice.getString("name");
                        lastName = notice.getString("lastName");
                        profession = notice.getString("profession");
                        city = notice.getString("city");
                        HashMap<String, String> oneNotice = new HashMap<>();
                        oneNotice.put("id", String.valueOf(id));
                        oneNotice.put("name", name);
                        oneNotice.put("lastName", lastName);
                        oneNotice.put("profession", profession);
                        oneNotice.put("city", city);

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

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "noticeList");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomListView adapter = new CustomListView(getActivity(), workerAll);
        workerList.setAdapter(adapter);
    }
}
