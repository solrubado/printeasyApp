package rubado.printeasy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubado.printeasy.Pojos.DashboardPojo;
import rubado.printeasy.PrintEasyApplication;
import rubado.printeasy.R;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private PrintEasyApplication mApp;
    private ProgressBar inkColorProgress;
    private ProgressBar inkBlackProgress;
    private ProgressBar pagesProgress;
    private TextView inkTricolorPercentage;
    private TextView inkBlackPercentage;
    private TextView pagesPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mApp = (PrintEasyApplication) getApplication();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        inkColorProgress = (ProgressBar) findViewById(R.id.progressBarInkColor);
        inkBlackProgress = (ProgressBar) findViewById(R.id.progressBarInkBlack);
        pagesProgress = (ProgressBar) findViewById(R.id.progressBarPages);
        inkTricolorPercentage = (TextView) findViewById(R.id.percentageInkTricolor);
        inkBlackPercentage = (TextView) findViewById(R.id.percentageInkBlack);
        pagesPercentage = (TextView) findViewById(R.id.percentagePages);

        showDashboardData();
    }

    private void showDashboardData() {
        Call<DashboardPojo> documentsCall = mApp.getAPI().dashboardCall();

        documentsCall.enqueue(new Callback<DashboardPojo>() {
            @Override
            public void onResponse(Call<DashboardPojo> call, Response<DashboardPojo> response) {
                if (response.code() >= 400 && response.code() <= 500) {
                    DashboardActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(DashboardActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    DashboardPojo dashboardPojo = response.body();

                    inkColorProgress.setProgress(dashboardPojo.getValues().get(0).getNumber());
                    inkTricolorPercentage.setText(dashboardPojo.getValues().get(0).getPorcentage());
                    inkBlackProgress.setProgress(dashboardPojo.getValues().get(1).getNumber());
                    inkBlackPercentage.setText(dashboardPojo.getValues().get(1).getPorcentage());

                    pagesProgress.setProgress(dashboardPojo.getValues().get(2).getNumber());
                    pagesPercentage.setText(dashboardPojo.getValues().get(2).getPorcentage());



                } else {
                    DashboardActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(DashboardActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DashboardPojo> call, Throwable t) {
                Log.e("DashboardActivity", t.getMessage());

            }

        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_close_session) {
            Intent i = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
