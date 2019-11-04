package com.kyle18003144.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

public class MainFragmentHostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SensorEventListener {

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    TextView txtFootsteps;
    int numSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment_host);

        firebaseAuth = FirebaseAuth.getInstance();

        txtFootsteps = findViewById(R.id.txtFootsteps);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        boolean specificFragment = getIntent().getIntExtra("fragment", -1) != -1;
        if (savedInstanceState == null && !specificFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_profile);
            toolbar.setTitle("My Profile");
        } else if (specificFragment) {
            // If another activity was opened and the user went back, should take them to the fragment where they started the new activity
            int fragment = getIntent().getIntExtra("fragment", 0);
            switch (fragment) {
                case R.id.nav_profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_profile);
                    toolbar.setTitle("My Profile");
                    break;
                case R.id.nav_progress:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProgressFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_progress);
                    toolbar.setTitle("My Progress");
                    break;
                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                    navigationView.setCheckedItem(R.id.nav_feed);
                    toolbar.setTitle("Feed");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        updateUI();

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        BroadcastsHelper.scheduleFirstFootstepUpload(getBaseContext());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FeedFragment()).commit();
                toolbar.setTitle("Feed");
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                toolbar.setTitle("My Profile");
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                toolbar.setTitle("Search");
                break;
            case R.id.nav_progress:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProgressFragment()).commit();
                toolbar.setTitle("My Progress");
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                toolbar.setTitle("Settings");
                break;
            case R.id.nav_log_out:
                firebaseAuth.signOut();
                Intent i = new Intent(MainFragmentHostActivity.this, SplashActivity.class);
                startActivity(i);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) e.values[0];// - counterSteps;
            SharedPrefsHelper.setTodayFootsteps(getBaseContext(), totalSteps);
            SharedPrefsHelper.setDate(getBaseContext(), new Date());
            numSteps = totalSteps - SharedPrefsHelper.getPreviousFootsteps(getBaseContext());
            updateUI();
        }
    }

    private void updateUI() {
        txtFootsteps.setText(numSteps + "");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
