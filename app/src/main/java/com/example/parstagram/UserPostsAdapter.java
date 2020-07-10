package com.example.parstagram;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.fragments.PostDetailsFragment;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.ViewHolder> {

    private ImageView mPostImageView;

    protected Context mContext;
    private List<Post> mPosts;

    public UserPostsAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mPosts.get(position);
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

    public void switchContent(Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.replaceFragment(fragment);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            mPostImageView = itemView.findViewById(R.id.postImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    Post post = mPosts.get(position);

                    if (position != RecyclerView.NO_POSITION) {
                        PostDetailsFragment fragment = new PostDetailsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Post.KEY_OBJECT_ID, post.getObjectId());
                        fragment.setArguments(bundle);
                        switchContent(fragment);
                    }
                }
            });
        }

        public void bind(Post post) {
            // Bind the post data to the view elements

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(mContext).load(image.getUrl()).into(mPostImageView);
            }
        }
    }
}
