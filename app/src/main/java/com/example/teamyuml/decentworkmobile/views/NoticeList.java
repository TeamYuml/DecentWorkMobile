package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.ListViewFragment;

public class NoticeList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Spinner panelSpinner;
    FragmentManager fragmentManager;

    private final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/";
    private final String WORKER_URL = VolleyInstance.getBaseUrl() + "/profiles/userProfiles/";
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        fragmentManager = this.getSupportFragmentManager();
        panelSpinner = findViewById(R.id.panel_spinner);
        panelSpinnerAdapter();

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                        "NoticeDetails",
                            "com.example.teamyuml.decentworkmobile.views"
                    ));

                    fragmentTransaction.replace(R.id.fragment_content, notice);
                } else if (parent.getItemIdAtPosition(position) == 1) {
                    Fragment worker = new ListViewFragment();

                    worker.setArguments(setParameters(
                        WORKER_URL,
                        R.id.workerList,
                        R.layout.activity_worker,
                        "getWorkers",
                        "WorkerDetails",
                            "com.example.teamyuml.decentworkmobile.views"
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

    private Bundle setParameters(String url, int listViewId, int listLayoutId, String methodName, String initClass, String packageName) {
        Bundle parameters = new Bundle();
        parameters.putString("url", url);
        parameters.putInt("listViewId", listViewId);
        parameters.putInt("listLayoutId", listLayoutId);
        parameters.putString("methodName", methodName);
        parameters.putString("initClass", initClass);
        parameters.putString("packageName", packageName);
        return parameters;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.login:
                Intent intent = new Intent(this, Login.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                break;
        }

        return true;
    }
}