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
import com.example.parstagram.databinding.FragmentUserBinding;
import com.example.parstagram.models.Post;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    private static final String TAG = "UserFragment";

    FragmentUserBinding binding;

    protected RecyclerView mFeedRecyclerView;
    private SwipeRefreshLayout mSwipeContainer;

    TextView mUsernameTextView;

    protected List<Post> mFeed;
    private ParseUser mUser;
    private UserPostsAdapter mAdapter;
    private String mUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUsernameTextView = binding.userNameTextView;
        mFeedRecyclerView = binding.feedRecyclerView;

        if (getArguments() != null) {
            mUsername = getArguments().getString(User.KEY_USERNAME);
        }

        mUsernameTextView.setText(mUsername);

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
        // Specify the user that the fragment should display
        try {
            getUser();

            // Specify which class to query
            ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
            query.whereEqualTo(Post.KEY_USER, mUser);
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
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void getUser() throws ParseException {
        // Specify which class to query
        ParseQuery<ParseUser> userQuery = ParseQuery.getQuery(ParseUser.class);
        userQuery.whereEqualTo(User.KEY_USERNAME, mUsername);
        mUser = userQuery.find().get(0);
    }
}
