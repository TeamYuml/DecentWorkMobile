package com.example.teamyuml.decentworkmobile.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamyuml.decentworkmobile.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Create own custom  ArrayAdapter ListView
 */
public class CustomWorkerView extends ArrayAdapter<HashMap<String, String>> {
    private Activity Worker;
    ArrayList<HashMap<String, String>> workersAll;

    public CustomWorkerView(Activity NoticeList, ArrayList<HashMap<String, String>> workersAll) {
        super(NoticeList, R.layout.workers_list, workersAll);
        this.Worker = NoticeList;
        this.workersAll = workersAll;
    }

    static class ViewHolder {
        public TextView name;
        public TextView lastName;
        public TextView profession;
        public TextView city;
    }

    @Override
    /**
     *  Initializes fields of viewHolder and adds it to view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = Worker.getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.workers_list, null, true);

        viewHolder = new ViewHolder();
        viewHolder.name = convertView.findViewById(R.id.name);
        viewHolder.lastName = convertView.findViewById(R.id.lastName);
        viewHolder.profession = convertView.findViewById(R.id.profession);
        viewHolder.city = convertView.findViewById(R.id.city);
        convertView.setTag(viewHolder);

        viewHolder.name.setText(workersAll.get(position).get("name"));
        viewHolder.lastName.setText(workersAll.get(position).get("lastName"));
        viewHolder.profession.setText(workersAll.get(position).get("profession").replace("[", "")
                .replace("\"", "")
                .replace(",", " ")
                .replace("]", ""));
        viewHolder.city.setText(workersAll.get(position).get("city"));

        return convertView;
    }
}