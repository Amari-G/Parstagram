package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parstagram.databinding.ActivityMainBinding;
import com.example.parstagram.fragments.FeedFragment;
import com.example.parstagram.fragments.NewPostFragment;
import com.example.parstagram.fragments.MyProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final FragmentManager fragmentManager = getSupportFragmentManager();

    Context context = this;
    ActivityMainBinding binding;

    Toolbar mToolbar;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the toolbar view and set as ActionBar
        mToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_camera);

        bottomNavigationView = binding.bottomNavigationView;
        setBottomNavItemSelectedListener();
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction().replace(binding.containerFrameLayout.getId(), fragment).commit();
    }

    private void setBottomNavItemSelectedListener() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.homeMenuItem:
                        fragment = new FeedFragment();

                        // Display icon in the toolbar
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        getSupportActionBar().setDisplayUseLogoEnabled(true);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        mToolbar.setElevation(8F);
                        break;
                    case R.id.newPostMenuItem:
                        fragment = new NewPostFragment();
                        break;
                    case R.id.userMenuItem:
                    default:
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        getSupportActionBar().setDisplayShowHomeEnabled(false);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                        fragment = new MyProfileFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(binding.containerFrameLayout.getId(), fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.homeMenuItem);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOutMenuItem:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);

                if (ParseUser.getCurrentUser() == null) Log.i(TAG, "User signed out");

                finish();
                return true;
            case R.id.composeMenuItem:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}