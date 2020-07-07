package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    ActivityMainBinding binding;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBottomNavItemSelectedListener();
    }

    private void setBottomNavItemSelectedListener() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.homeMenuItem:
                        Toast.makeText(MainActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.newPostMenuItem:
                        Toast.makeText(MainActivity.this, "NEW POST", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.userMenuItem:
                    default:
                        Toast.makeText(MainActivity.this, "PROFILE", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}