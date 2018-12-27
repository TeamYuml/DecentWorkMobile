package com.example.teamyuml.decentworkmobile.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.teamyuml.decentworkmobile.BuildConfig;
import com.example.teamyuml.decentworkmobile.R;
import com.example.teamyuml.decentworkmobile.VolleyInstance;
import com.example.teamyuml.decentworkmobile.fragments.ListViewFragment;
import com.example.teamyuml.decentworkmobile.fragments.NoticeForm;
import com.example.teamyuml.decentworkmobile.utils.UserAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class NoticeList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fragmentManager;

    private final String NOTICE_URL = VolleyInstance.getBaseUrl() + "/engagments/engagments/";
    private final String WORKER_URL = VolleyInstance.getBaseUrl() + "/profiles/withProfession/";
    private final String USER_NOTICES_URL = VolleyInstance.getBaseUrl() + "/engagments/user/engagments/";

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        fragmentManager = this.getSupportFragmentManager();
        drawerLayout = findViewById(R.id.drawer_layout);

        initToolbar();

        Fragment notice = new ListViewFragment();

        initNoticeArgument(notice);
        initFragmentReplacer(notice);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        adjustMenu(navigationView.getMenu());
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment notice = new ListViewFragment();

        switch (menuItem.getItemId()) {
            case R.id.login:
                Intent intent = new Intent(this, Login.class);
                drawerLayout.closeDrawers();
                startActivity(intent);
                break;
            case R.id.logout:
                this.logout();
                break;
            case R.id.userPanel:
                Intent userPanel = new Intent(this, UserPanel.class);
                drawerLayout.closeDrawers();
                startActivity(userPanel);
                break;
            case R.id.userNotices:
                userNoticeFragment();
                drawerLayout.closeDrawers();
                break;
            case R.id.noticeList:
                initNoticeArgument(notice);
                initFragmentReplacer(notice);
                drawerLayout.closeDrawers();
                break;
            case R.id.workerList:
                Fragment worker = new ListViewFragment();

                worker.setArguments(setParameters(
                    WORKER_URL,
                    R.id.workerList,
                    R.layout.activity_worker,
                    "getWorkers",
                    "WorkerDetails"
                ));

                initFragmentReplacer(worker);
                drawerLayout.closeDrawers();
                break;
            case R.id.addNotice:
                Fragment addNotice = new NoticeForm();
                initFragmentReplacer(addNotice);
                drawerLayout.closeDrawers();
                break;
        }

        return true;
    }

    /**
     * Check if user is logged and adjust menu items to it.
     * @param menu - Navigation view menu.
     */
    private void adjustMenu(Menu menu) {
        if (UserAuth.getToken(NoticeList.this) != null) {
            menu.removeItem(R.id.login);
        } else {
            menu.removeItem(R.id.logout);
            menu.removeItem(R.id.userPanel);
            menu.removeItem(R.id.userNotices);
        }
    }

    private void logout() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GoogleClientID)
                .requestEmail()
                .build();

            GoogleSignInClient gsic = GoogleSignIn.getClient(this, gso);

            gsic.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    UserAuth.logoutUser(NoticeList.this);
                    finish();
                    startActivity(getIntent());
                }
            });
        } else {
            UserAuth.logoutUser(NoticeList.this);
            finish();
            startActivity(getIntent());
        }
    }

    /*
     * Initialize toolbar properties
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    private void initFragmentReplacer(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initNoticeArgument(Fragment fragmentList) {
        fragmentList.setArguments(setParameters(
            NOTICE_URL,
            R.id.noticeList,
            R.layout.notice_list_view,
            "getNotice",
            "NoticeDetails"
        ));
    }

    private void userNoticeFragment() {
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
}