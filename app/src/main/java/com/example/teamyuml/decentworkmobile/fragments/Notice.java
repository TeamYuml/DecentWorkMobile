package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.views.CustomListView;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;
import com.example.teamyuml.decentworkmobile.views.NoticeList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Notice extends Fragment {
    ArrayList<HashMap<String, String>> noticeAll;
    private ListView noticeList;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getNotice();
        super.onCreate(savedInstanceState);
        noticeAll = new ArrayList<>();
        System.out.println("DIALA");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View v = inflater.inflate(R.layout.notice_list_view, container, false);
        noticeList = v.findViewById(R.id.noticeList);
        return v;
    }

    private void getNotice() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, NOTICE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String profession = null;
                String title = null;
                Integer id = null;

                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject notice = results.getJSONObject(i);
                        id = notice.getInt("id");
                        title = notice.getString("title");
                        profession = notice.getString("profession");

                        HashMap<String, String> oneNotice = new HashMap<>();
                        oneNotice.put("id", String.valueOf(id));
                        oneNotice.put("title", title);
                        oneNotice.put("profession", profession);

                        noticeAll.add(oneNotice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (noticeAll != null) {
                    initNoticeList();
                    Toast.makeText(getActivity(),
                            "Pobrano " + noticeAll.size() + " ogłoszenia",
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

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Notice");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomListView adapter = new CustomListView(getActivity(), noticeAll);
        noticeList.setAdapter(adapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clickedItem = noticeAll.get(position).get("id").toString();
                Intent toNoticeDatail = new Intent(getActivity(), NoticeDetails.class);
                toNoticeDatail.putExtra("choosenNotice", (String) clickedItem);
                startActivity(toNoticeDatail);
            }
        });
    }
}
