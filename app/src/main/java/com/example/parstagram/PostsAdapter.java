package com.example.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.fragments.PostDetailsFragment;
import com.example.parstagram.fragments.UserFragment;
import com.example.parstagram.models.Post;
import com.example.parstagram.models.User;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    protected Context mContext;
    private List<Post> mPosts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ItemPostBinding binding =
                ItemPostBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        mPosts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> posts) {
        posts.addAll(posts);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemPostBinding binding;

        private ImageView userImageView;
        private ImageView postImageView;
        private TextView userNameTextView;
        private TextView captionUserNameTextView;
        private TextView postCaptionTextView;
        private TextView postTimestampTextView;


        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            userImageView = binding.userImageView;
            postImageView = binding.postImageView;
            userNameTextView = binding.userNameTextView;
            captionUserNameTextView = binding.captionUserNameTextView;
            postCaptionTextView = binding.postCaptionTextView;
            postTimestampTextView = binding.postTimestampTextView;
        }


        public void bind(Post post) {
            // Bind the post data to the view elements
            postCaptionTextView.setText(post.getDescription());
            userNameTextView.setText(post.getUser().getUsername());
            userNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserFragment fragment = new UserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(User.KEY_USERNAME, userNameTextView.getText().toString());
                    fragment.setArguments(bundle);
                    switchContent(fragment);
                }
            });

            captionUserNameTextView.setText(post.getUser().getUsername());
            captionUserNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserFragment fragment = new UserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(User.KEY_USERNAME, captionUserNameTextView.getText().toString());
                    fragment.setArguments(bundle);
                    switchContent(fragment);
                }
            });

            Date date = post.getCreatedAt();
            String elapsedTime = ParseDate.getElapsedTime(date);
            postTimestampTextView.setText(elapsedTime);

            Glide.with(mContext).load(mContext.getDrawable(R.drawable.instagram_user_filled_24)).transform(new CircleCrop()).into(userImageView);

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(mContext).load(image.getUrl()).into(postImageView);
            }
        }

        public void switchContent(Fragment fragment) {
            if (mContext == null)
                return;
            if (mContext instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.replaceFragment(fragment);
            }

        }
    }
}
