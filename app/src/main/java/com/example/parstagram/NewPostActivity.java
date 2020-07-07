package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.parstagram.databinding.ActivityNewPostBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";

    private ActivityNewPostBinding binding;
    
    Button mCaptureImageButton;
    Button mShareButton;
    EditText mCaptionEditText;
    ImageView mPostPictureImageView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mCaptureImageButton = binding.captureImageButton;
        mShareButton = binding.shareButton;
        mCaptionEditText = binding.captionEditText;
        mPostPictureImageView = binding.postPictureImageView;

//        queryPosts();

        setOnClickListeners();
    }

    private void setOnClickListeners() {
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = mCaptionEditText.getText().toString();
                if (description.isEmpty()) {
                    Toast.makeText(NewPostActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser);
            }
        });
    }

    private void savePost(String description, ParseUser currentUser) {
        Post post = new Post();
        post.setKeyDescription(description);
//        post.setKeyImage(image);
        post.setKeyUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post", e);
                    Toast.makeText(NewPostActivity.this, "Error while saving :(", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post saved successfully!");
                mCaptionEditText.setText(" ");
            }
        });
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post  .class);
        query.include(Post.KEY_USER);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Problem  with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getKeyUser().getUsername());
                }
            }
        });
    }
}