package com.example.parstagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parstagram.LoginActivity;
import com.example.parstagram.UserPostsAdapter;
import com.example.parstagram.databinding.FragmentMyProfileBinding;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyProfileFragment extends UserFragment {

    private static final String TAG = "UserFragment";

    FragmentMyProfileBinding binding;

    protected RecyclerView mFeedRecyclerView;
    private SwipeRefreshLayout mSwipeContainer;

    TextView mUsernameTextView;
    Button mLogOutButton;

    protected List<Post> mFeed;

    private UserPostsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUsernameTextView = binding.userNameTextView;
        mLogOutButton = binding.logOutButton;
        mFeedRecyclerView = binding.feedRecyclerView;

        mUsernameTextView.setText(ParseUser.getCurrentUser().getUsername());

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);

                if (ParseUser.getCurrentUser() == null) Log.i(TAG, "User signed out");

                getActivity().finish();
            }
        });

        // Lookup the swipe container view
        mSwipeContainer = binding.swipeContainer;
        // Setup refresh listener which triggers new data loading
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });

        // populate recycler view
        mFeed = new ArrayList<>();
        mAdapter = new UserPostsAdapter(getContext(), mFeed);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);

        mAdapter = new UserPostsAdapter(getContext(), mFeed);

        mFeedRecyclerView.setAdapter(mAdapter);
        mFeedRecyclerView.setLayoutManager(gridLayoutManager);
        queryPosts();
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Problem  with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                mAdapter.clear();
                mFeed.addAll(posts);
                mAdapter.notifyDataSetChanged();
                mSwipeContainer.setRefreshing(false);
            }
        });
    }
}
