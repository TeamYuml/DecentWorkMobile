package com.example.teamyuml.decentworkmobile.views;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.teamyuml.decentworkmobile.R;

public class NoticeList extends AppCompatActivity {

    private ListView noticeList;
    private String[] testStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        noticeList = findViewById(R.id.noticeList);
        testStrings = new String[] {
                "A", "B", "C"
        };
        initNoticeList();
    }

    private void initNoticeList() {
        noticeList.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1, testStrings
        ));
    }

    private void initLanguagesListView() {
        noticeList.setAdapter(new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                testStrings));

        noticeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                Toast.makeText(getApplicationContext(),
                        testStrings[pos],
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
