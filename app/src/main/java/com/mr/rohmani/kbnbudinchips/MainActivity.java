package com.mr.rohmani.kbnbudinchips;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Fragment fragment = null;
    private SharedPreferences settings;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_beranda:
                    fragment = new Beranda();
                    callFragment(fragment);
                    return true;
                case R.id.nav_stok:
                    fragment = new Stok();
                    callFragment(fragment);
                    return true;
                case R.id.nav_keuangan:
                    fragment = new Keuangan();
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
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("SharedPrefences",MODE_PRIVATE);
        manager = getFragmentManager();
        String token = settings.getString("token", "");

        if (savedInstanceState == null) {
            fragment = new Beranda();
            callFragment(fragment);
        }

        if (!token.isEmpty()){
            HashMap<String, Object> result = new HashMap<>();
            result.put("NotificationToken", token);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(getUid()).updateChildren(result);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("token", "");
            editor.commit();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("pemesananBaru");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void callFragment(Fragment fragment) {
        transaction = manager.beginTransaction();

        transaction.remove(fragment);
        transaction.replace(R.id.main_layout, fragment);
        transaction.commit();
    }

}
