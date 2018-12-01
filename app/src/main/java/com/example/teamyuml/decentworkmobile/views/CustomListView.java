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
public class CustomListView extends ArrayAdapter<HashMap<String, String>> {
    private Activity NoticeList;
    ArrayList<HashMap<String, String>> noticeAll;

    public CustomListView(Activity NoticeList, ArrayList<HashMap<String, String>> noticeAll) {
        super(NoticeList, R.layout.notice_list, noticeAll);
        this.NoticeList = NoticeList;
        this.noticeAll = noticeAll;
    }

    static class ViewHolder {
        public TextView id;
        public TextView title;
        public TextView profession;
    }

    @Override
    /**
     *  Initializes fields of viewHolder and adds it to view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = NoticeList.getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.notice_list, null, true);

        viewHolder = new ViewHolder();
        viewHolder.id = convertView.findViewById(R.id.id);
        viewHolder.title = convertView.findViewById(R.id.title);
        viewHolder.profession = convertView.findViewById(R.id.profession);
        convertView.setTag(viewHolder);

        viewHolder.id.setText(noticeAll.get(position).get("id").toString());
        viewHolder.title.setText(noticeAll.get(position).get("title").toString());
        viewHolder.profession.setText(noticeAll.get(position).get("profession").toString());

        return convertView;
    }
}