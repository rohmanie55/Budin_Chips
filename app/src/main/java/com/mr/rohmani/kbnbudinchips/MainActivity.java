package com.mr.rohmani.kbnbudinchips;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment fragment = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_beranda:
                    fragment = new Beranda();
                    callFragment(fragment);
                    return true;
                case R.id.nav_pemesanan:
                    fragment = new Pemesanan();
                    callFragment(fragment);
                    return true;
                case R.id.nav_laporan:
                    fragment = new Laporan();
                    callFragment(fragment);
                    return true;
                case R.id.nav_chat:
                    fragment = new Chating();
                    callFragment(fragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            fragment = new Beranda();
            callFragment(fragment);
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void callFragment(Fragment fragment) {
        transaction = manager.beginTransaction();

        transaction.remove(fragment);
        transaction.replace(R.id.main_layout, fragment);
        transaction.commit();
    }

}
