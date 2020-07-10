package com.example.parstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.parstagram.databinding.FragmentUserBinding;
import com.example.parstagram.databinding.ItemPostBinding;
import com.example.parstagram.models.Post;
import com.parse.ParseFile;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    protected Context context;
    private List<Post> mPosts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
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
            captionUserNameTextView.setText(post.getUser().getUsername());

            Date date = post.getCreatedAt();
            String elapsedTime = ParseDate.getElapsedTime(date);
            postTimestampTextView.setText(elapsedTime);

            Glide.with(context).load(context.getDrawable(R.drawable.instagram_user_filled_24)).transform(new CircleCrop()).into(userImageView);

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(postImageView);
            }
        }
    }
}
