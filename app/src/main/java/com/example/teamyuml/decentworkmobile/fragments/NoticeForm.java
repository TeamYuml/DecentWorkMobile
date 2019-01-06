package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.database.DBHelper;
import com.example.teamyuml.decentworkmobile.utils.CreateJson;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NoticeForm extends Fragment implements View.OnClickListener {

    private View v;
    private int dropDownLayout = android.R.layout.simple_dropdown_item_1line;
    private EditText noticeTitle;
    private EditText noticeDescription;
    private EditText noticeProfession;
    private EditText noticeCity;
    private final String NOTICE_ADD_URL = VolleyInstance.getBaseUrl() + "/notices/notices/";
    private final String USER_NOTICES_URL = VolleyInstance.getBaseUrl() + "/notices/user/notices/";
    private final String EDIT_NOTICE_URL = VolleyInstance.getBaseUrl() +"/notices/notices/";
    private String RESPONSE_URL = null;
    private int requestMethod;
    FragmentManager fragmentManager;
    private Button cancel_btn;
    DBHelper myDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.notice_form, container, false);
        noticeTitle = v.findViewById(R.id.title);
        noticeDescription = v.findViewById(R.id.description);
        noticeProfession = v.findViewById(R.id.profession);
        noticeCity = v.findViewById(R.id.city);
        fragmentManager = getActivity().getSupportFragmentManager();

        v.findViewById(R.id.add_notice_btn).setOnClickListener(this);
        cancel_btn = v.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                backToList(v);
            }
        });

        cancel_btn.setVisibility(View.GONE);

        RESPONSE_URL = NOTICE_ADD_URL;
        requestMethod = Request.Method.POST;

        Bundle fields = getArguments();

        if (fields != null) {
            noticeTitle.setText(fields.getString("title"));
            noticeDescription.setText(fields.getString("description"));
            noticeProfession.setText(fields.getString("profession"));
            noticeCity.setText(fields.getString("city"));

            cancel_btn.setVisibility(View.VISIBLE);
            RESPONSE_URL = EDIT_NOTICE_URL + getArguments().getInt("id") + "/";
            requestMethod = Request.Method.PUT;
        }

        return v;
    }

    private void backToList(View v) {
        Fragment notice = new ListViewFragment();

        notice.setArguments(setParameters(
            NOTICE_ADD_URL,
            R.id.noticeList,
            R.layout.notice_list_view,
            "getNotice",
            "NoticeDetails"
        ));

        initFragmentReplacer(notice);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                requestMethod, RESPONSE_URL, addParams(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Fragment notice = new ListViewFragment();

                    notice.setArguments(setParameters(
                        USER_NOTICES_URL,
                        R.id.noticeList,
                        R.layout.notice_list_view,
                        "getUserNotice",
                        "NoticeDetails"
                    ));

                    initFragmentReplacer(notice);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ErrorHandler.errorHandler(error, getActivity());
                }
            }) {
                public Map<String, String> getHeaders() {
                    return UserAuth.authorizationHeader(getActivity());
                }
            };

            VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Create notice");
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "Nie można sparsować parametrów", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method is responsibility for adding params to engagment
     * @return JSONObject object
     */
    private JSONObject addParams() throws JSONException {
        String title = noticeTitle.getText().toString();
        String description = noticeDescription.getText().toString();
        String city = noticeCity.getText().toString();
        String profession = noticeProfession.getText().toString();

        CreateJson cj = new CreateJson();
        cj.addStr("city", city);
        cj.addStr("title" , title);
        cj.addStr("description", description);
        cj.addStr("profession", profession);

        return cj.makeJSON();
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

    private void initFragmentReplacer(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
