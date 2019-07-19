package com.example.jahadulrakib.servicehut.User;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jahadulrakib.servicehut.Profile;
import com.example.jahadulrakib.servicehut.R;


public class MainActivity extends AppCompatActivity {

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
            fragment = new HomeFragment();
        }
        else if (id == R.id.navigation_history){
            fragment = new Favourate();
        }
        else if (id == R.id.navigation_notifications){
            fragment = new Notification();
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
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation =(BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        HomeFragment homeFragment = new HomeFragment();
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