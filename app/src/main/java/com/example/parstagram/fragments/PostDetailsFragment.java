package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.parstagram.ParseDate;
import com.example.parstagram.databinding.FragmentPostDetailsBinding;
import com.example.parstagram.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.Date;

public class PostDetailsFragment extends Fragment {

    private static final String TAG = "PostDetailsFragment";

    FragmentPostDetailsBinding binding;

    private String mPostId;
    private Post mPost;

    private ImageView userImageView;
    private ImageView postImageView;
    private TextView userNameTextView;
    private TextView postCaptionTextView;
    private TextView postTimestampTextView;

    public PostDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostDetailsBinding.inflate(inflater, container, false);

        userImageView = binding.userImageView;
        postImageView = binding.postImageView;
        userNameTextView = binding.userNameTextView;
        postCaptionTextView = binding.postCaptionTextView;
        postTimestampTextView = binding.postTimestampTextView;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mPostId = getArguments().getString(Post.KEY_OBJECT_ID);
        }

        try {
            getPost();
            Log.i(TAG, "Found post with the Object ID: " + mPostId);

            // Bind the post data to the view elements
            postCaptionTextView.setText(mPost.getDescription());
            userNameTextView.setText(mPost.getUser().getUsername());

            Date date = mPost.getCreatedAt();
            String elapsedTime = ParseDate.getElapsedTime(date);
            postTimestampTextView.setText(elapsedTime);

            Log.i(TAG, "Created " + elapsedTime);

            ParseFile image = mPost.getImage();

            if (image != null) {
                Glide.with(this).load(image.getUrl()).into(postImageView);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not find post with the Object ID: " + mPostId, e);
        }
    }

    protected void getPost() throws ParseException {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        mPost = query.get(mPostId);
    }
}