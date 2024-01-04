package com.example.qatarworldcup;



import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager vp = findViewById(R.id.viewpager);
        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);

        TabLayout tab = findViewById(R.id.tab);
        tab.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu1:

                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

                Toast.makeText(this, "조별리그로 이동합니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu2:
                in = new Intent(this,tournament.class);
                startActivity(in);

                Toast.makeText(this, "토너먼트로 이동합니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.menu3:
                setContentView(R.layout.activity_favorite_team);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
