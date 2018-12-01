package com.example.teamyuml.decentworkmobile.views;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class NoticeList extends AppCompatActivity {

    ArrayList<HashMap<String, String>> noticeAll;
    private ListView noticeList;
    private static final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getNotice();
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        noticeList = findViewById(R.id.noticeList);
        noticeAll = new ArrayList<>();
    }

    private void getNotice() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, NOTICE_URL, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String profession = null;
                        String title = null;
                        Integer id = null;

                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject notice = response.getJSONObject(i);
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
                                    "Pobrano " + noticeAll.size() + " ogłoszenia"
                                    , Toast.LENGTH_LONG).show();
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

        VolleyInstance.getInstance(this).addToRequestQueue(jsonArrayRequest, "noticeList");
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initNoticeList() {
        CustomListView adapter = new CustomListView(this, noticeAll);
        noticeList.setAdapter(adapter);

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                noticeAll.get(position);
            }
        });
    }
}
