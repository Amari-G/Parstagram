package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.parstagram.databinding.ActivityMainBinding;
import com.example.parstagram.fragments.FeedFragment;
import com.example.parstagram.fragments.NewPostFragment;
import com.example.parstagram.fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    ActivityMainBinding binding;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNavigationView;
        setBottomNavItemSelectedListener();
    }

    private void setBottomNavItemSelectedListener() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.homeMenuItem:
                        fragment = new FeedFragment();
                        break;
                    case R.id.newPostMenuItem:
                        fragment = new NewPostFragment();
                        break;
                    case R.id.userMenuItem:
                    default:
                        fragment = new UserFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(binding.containerFrameLayout.getId(), fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
    }
}