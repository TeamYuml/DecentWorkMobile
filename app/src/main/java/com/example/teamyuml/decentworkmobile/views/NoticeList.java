package com.example.teamyuml.decentworkmobile.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.teamyuml.decentworkmobile.fragments.ListViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class NoticeList extends AppCompatActivity {

    private ListView noticeList;
    Spinner panelSpinner;
    FragmentManager fragmentManager;

    private final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/";
    private final String WORKER_URL = VolleyInstance.getBaseUrl() + "/profiles/four/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        fragmentManager = this.getSupportFragmentManager();
        panelSpinner = findViewById(R.id.panel_spinner);
        panelSpinnerAdapter();
    }

    private void panelSpinnerAdapter() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.notice_list, android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        panelSpinner.setAdapter(spinnerAdapter);

        panelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (parent.getItemIdAtPosition(position) == 0) {
                    Fragment notice = new ListViewFragment();

                    notice.setArguments(setParameters(
                        NOTICE_URL,
                        R.id.noticeList,
                        R.layout.notice_list_view,
                        "getNotice",
                            "noticesList"
                    ));

                    fragmentTransaction.replace(R.id.fragment_content, notice);
                } else if (parent.getItemIdAtPosition(position) == 1) {
                    Fragment worker = new ListViewFragment();

                    worker.setArguments(setParameters(
                        WORKER_URL,
                        R.id.workerList,
                        R.layout.activity_worker,
                        "getWorkers",
                            "workersList"
                    ));

                    fragmentTransaction.replace(R.id.fragment_content, worker);
                }

                //progress dialog
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(NoticeList.this, "Nic nie wybrałes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bundle setParameters(String url, int listViewId, int listLayoutId, String methodName, String initClass) {
        Bundle parameters = new Bundle();
        parameters.putString("url", url);
        parameters.putInt("listViewId", listViewId);
        parameters.putInt("listLayoutId", listLayoutId);
        parameters.putString("methodName", methodName);
        parameters.putString("initClass", initClass);
        return parameters;
    }
}
