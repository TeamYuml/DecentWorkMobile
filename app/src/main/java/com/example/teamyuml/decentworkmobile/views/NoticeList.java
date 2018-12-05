package com.example.teamyuml.decentworkmobile.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class NoticeList extends AppCompatActivity {

    ArrayList<HashMap<String, String>> noticeAll;
    private ListView noticeList;
    Spinner panelSpinner;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getNotice();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        panelSpinner = findViewById(R.id.panel_spinner);
        panelSpinnerAdapter();
        noticeList = findViewById(R.id.noticeList);
        noticeAll = new ArrayList<>();

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
                            Toast.makeText(NoticeList.this,
                                    "Pobrano " + noticeAll.size() + " ogłoszenia",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NoticeList.this,
                                "Coś poszło nie tak",
                                Toast.LENGTH_LONG).show();
                    }
                });

        VolleyInstance.getInstance(this).addToRequestQueue(jsonObjectRequest, "noticeList");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomListView adapter = new CustomListView(this, noticeAll);
        noticeList.setAdapter(adapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clickedItem = noticeAll.get(position).get("id").toString();
                Intent toNoticeDatail = new Intent(NoticeList.this, NoticeDetails.class);
                toNoticeDatail.putExtra("choosenNotice", (String) clickedItem);
                startActivity(toNoticeDatail);

            }
        });
    }

    private void panelSpinnerAdapter() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.notice_list, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panelSpinner.setAdapter(spinnerAdapter);

        panelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemIdAtPosition(position) == 1) {
                    Intent toWorker = new Intent(NoticeList.this ,Worker.class);
                    startActivity(toWorker);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NoticeList.this, "Nic nie wybrałes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
