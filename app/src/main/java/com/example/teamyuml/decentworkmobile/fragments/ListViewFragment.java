package com.example.teamyuml.decentworkmobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.views.CustomListView;
import com.example.teamyuml.decentworkmobile.views.CustomWorkerView;
import com.example.teamyuml.decentworkmobile.views.NoticeDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewFragment extends Fragment {
    private ArrayList<HashMap<String, String>> data;
    private ListView listView;
    private String URL;
    private int listViewId;
    private int listLayoutId;
    private Method callMethod;
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        View v = inflater.inflate(listLayoutId, container, false);
        listView = v.findViewById(listViewId);
        System.out.println(listView);
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
                Intent toNoticeDetail = new Intent(getActivity(), NoticeDetails.class);
                toNoticeDetail.putExtra("choosenNotice", (String) clickedItem);
                startActivity(toNoticeDetail);
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
                        int id = notice.getInt("id");
                        String title = notice.getString("title");
                        String profession = notice.getString("profession");

                        HashMap<String, String> oneNotice = new HashMap<>();
                        oneNotice.put("id", String.valueOf(id));
                        oneNotice.put("title", title);
                        oneNotice.put("profession", profession);

                        data.add(oneNotice);
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
                Toast.makeText(getActivity(),
                    "Coś poszło nie tak",
                    Toast.LENGTH_LONG).show();
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest, "Notice");
    }

    private void getWorkers() {
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject profile = response.getJSONObject(i);
                        JSONObject user = profile.getJSONObject("user");
                        String id = String.valueOf(user.getInt("id"));
                        String name = user.getString("first_name");
                        String last_name = user.getString("last_name");

                        /*TODO: Professions is a list so we have to
                          think how we want to store it in viewHolder*/
                        JSONArray professionsJson = (JSONArray) profile.get("professions");
                        List<String> professions = new ArrayList<>();
                        for (int j = 0; j < professionsJson.length(); j++) {
                            professions.add(professionsJson.getString(j));
                        }

                        HashMap<String, String> oneNotice = new HashMap<>();

                        /**
                         * This is needed but in adapter this field is not added for now:
                         * oneNotice.put("id", id);
                         */

                        oneNotice.put("name", name);
                        oneNotice.put("lastName", last_name);
                        oneNotice.put("city", profile.getString("city"));

                        data.add(oneNotice);
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
                Toast.makeText(getActivity(),
                    "Coś poszło nie tak",
                    Toast.LENGTH_LONG).show();
            }
        });

        VolleyInstance.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest, "workers");
    }
}
