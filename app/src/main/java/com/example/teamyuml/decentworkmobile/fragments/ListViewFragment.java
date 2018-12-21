package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.example.teamyuml.decentworkmobile.views.CustomListView;
import com.example.teamyuml.decentworkmobile.views.CustomWorkerView;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;
import com.example.teamyuml.decentworkmobile.views.WorkerDetails;
import com.example.teamyuml.decentworkmobile.volley.ErrorHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewFragment extends Fragment {
    private ArrayList<HashMap<String, String>> data;
    private ListView listView;
    private String URL;
    private int listViewId;
    private int listLayoutId;
    private Method callMethod;
    private String initClass;
    private String packageName = "com.example.teamyuml.decentworkmobile.views";
    private ArrayAdapter<HashMap<String, String>> adapterClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        Bundle fields = getArguments();
        data = new ArrayList<>();

        try {
            if (fields != null) {
                initializeFragment(fields);
                callMethod.invoke(this);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Toast.makeText(
                getActivity() ,"Brak dostępu do metody.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        View v = inflater.inflate(listLayoutId, container, false);
        listView = v.findViewById(listViewId);
        return v;
    }

    /**
     * Initialize fragment fields.
     * @param bundle - Bundle from {@link Fragment#getArguments()} method
     */
    private void initializeFragment(Bundle bundle) throws NoSuchMethodException {
        URL = bundle.getString("url");
        listViewId = bundle.getInt("listViewId");
        listLayoutId = bundle.getInt("listLayoutId");
        String methodName = bundle.getString("methodName");
        initClass = bundle.getString("initClass");

        if (methodName != null) {
            callMethod = getClass().getDeclaredMethod(methodName);
            callMethod.setAccessible(true);
        } else {
            throw new NoSuchMethodException();
        }
    }

    /**
     * Set adapter to ListView and add clickListener event to get clicked data
     */
    private void initListView() {
        listView.setAdapter(adapterClass);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String clickedItem = data.get(position).get("id").toString();
                try {
                    Intent toSelectedDetail = new Intent(getActivity(), Class.forName(packageName + "." + initClass));
                    toSelectedDetail.putExtra("choosenProfile", (String) clickedItem);
                    startActivity(toSelectedDetail);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getNotice() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject notice = results.getJSONObject(i);
                        getNoticeResponse(notice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (data != null) {
                    adapterClass = new CustomListView(getActivity(), data);
                    initListView();
                    Toast.makeText(getActivity(),
                        "Pobrano " + data.size() + " ogłoszenia",
                        Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Notice");
    }

    private void getUserNotice() {
        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject notice = response.getJSONObject(i);
                        getNoticeResponse(notice);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (data != null) {
                    adapterClass = new CustomListView(getActivity(), data);
                    initListView();
                    Toast.makeText(getActivity(),
                            "Pobrano " + data.size() + " ogłoszenia",
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return UserAuth.authorizationHeader(getActivity());
            }
        };

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Notice");
    }

    private void getWorkers() {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject profile = results.getJSONObject(i);
                        JSONObject user = profile.getJSONObject("user");
                        String id = String.valueOf(user.getInt("id"));
                        String name = user.getString("first_name");
                        String last_name = user.getString("last_name");
                        HashMap<String, String> oneWorker = new HashMap<>();
                        oneWorker.put("id", id);
                        oneWorker.put("name", name);
                        oneWorker.put("lastName", last_name);
                        oneWorker.put("city", profile.getString("city"));
                        oneWorker.put("profession", profile.getString("professions"));
                        data.add(oneWorker);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (data != null) {
                    adapterClass = new CustomWorkerView(getActivity(), data);
                    initListView();
                    Toast.makeText(getActivity(),
                        "Pobrano " + data.size() + " profili",
                        Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorHandler.errorHandler(error, getActivity());
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "workers");
    }

    private void getNoticeResponse(JSONObject notice) throws JSONException {
        HashMap<String, String> oneNotice = new HashMap<>();
        oneNotice.put("id", String.valueOf(notice.getInt("id")));
        oneNotice.put("title", notice.getString("title"));
        oneNotice.put("profession", notice.getString("profession"));
        data.add(oneNotice);
    }
}
