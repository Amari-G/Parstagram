package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivitySignUpBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    ActivitySignUpBinding binding;

    EditText mUsernameEditText;
    EditText mPasswordEditText;
    EditText mConfirmPasswordEditText;
    Button mLoginButton;
    ProgressBar mProgressBar;
    TextView mLogInTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        mProgressBar = binding.loadingProgressBar;

        mUsernameEditText = binding.usernameEditText;
        mUsernameEditText.requestFocus();

        mPasswordEditText = binding.passwordEditText;
        mConfirmPasswordEditText = binding.confirmPasswordEditText;

        mLoginButton = binding.loginButton;
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                signUpUser(username, password);
            }
        });


        mLogInTextView = binding.logInTextView;
        String first = "Don't have an account? ";
        String next = "<font color='#3B5998'><b>Sign up.</b></font>";
        mLogInTextView.setText(Html.fromHtml(first + next));
        mLogInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLoginActivity();
            }
        });
    }

    private void signUpUser(String username, String password) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        Log.i(TAG, "Attempting to sign user up" + username);
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(mUsernameEditText.getText().toString());
        user.setPassword(mPasswordEditText.getText().toString());
//        user.setEmail("email@example.com");
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with login: ", e);
                    Toast.makeText(SignUpActivity.this, "Issue with sign up.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
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

    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}