package com.example.jahadulrakib.servicehut.Plumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jahadulrakib.servicehut.R;
import com.example.jahadulrakib.servicehut.Profile;

public class MainPlumberProfile extends AppCompatActivity {
    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            display(id);
            return true;
        }

    };

    private void display(int id) {

        Fragment fragment = null;
        if (id == R.id.navigation_home){
            fragment = new PlumberProfile();
        }
        else if (id == R.id.navigation_history){
            fragment = new PlumberJobHisotry();
        }
        else if (id == R.id.navigation_more){
            fragment = new Profile();
        }
        if (fragment != null){
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver_profile);
        BottomNavigationView navigation =(BottomNavigationView) findViewById(R.id.navigations);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        PlumberProfile homeFragment = new PlumberProfile();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, homeFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        finish();
        super.onBackPressed();
    }
}
