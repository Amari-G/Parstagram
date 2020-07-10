package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parstagram.databinding.ActivityMainBinding;
import com.example.parstagram.fragments.FeedFragment;
import com.example.parstagram.fragments.NewPostFragment;
import com.example.parstagram.fragments.MyProfileFragment;
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

        // Find the toolbar view and set as ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        // ...
        // Display icon in the toolbar

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feed_toolbar_menu, menu);
        return true;
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
                        break;
                    case R.id.newPostMenuItem:
                        fragment = new NewPostFragment();
                        break;
                    case R.id.userMenuItem:
                    default:
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
}