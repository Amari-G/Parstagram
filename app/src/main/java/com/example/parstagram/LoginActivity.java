package com.example.parstagram;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parstagram.R;
import com.example.parstagram.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        mProgressBar = binding.loadingProgressBar;
        mUsernameEditText = binding.usernameEditText;
        mPasswordEditText = binding.passwordEditText;
        mLoginButton = binding.loginButton;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                loginUser(username, password);
            }
        });

        mUsernameEditText.requestFocus();
    }

    private void loginUser(String username, String password) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Log.i(TAG, "Attempting to login user " + username);
        // TODO: check credentials and progress user
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                //TODO: better error handling
                if (e != null) {
                    Log.e(TAG, "Issue with login: ", e);
                    Toast.makeText(LoginActivity.this, "Issue with login :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                goMainActivity();
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}