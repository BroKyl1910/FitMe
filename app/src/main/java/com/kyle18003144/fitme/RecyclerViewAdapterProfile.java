package com.kyle18003144.fitme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.kyle18003144.fitme.UnitsHelper.convertToImperialLbs;


public class RecyclerViewAdapterProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LinearLayout parentLayout;
    Context context;
    ViewHolder viewHolder;
    ArrayList<AppPost> posts = new ArrayList<>();

    TextView txtPostTitle;
    TextView txtPostProgress;
    TextView txtPostBody;
    ImageView imgPostImage;
    TextView txtPostDate;

    public RecyclerViewAdapterProfile(Context context, ArrayList<AppPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    public RecyclerViewAdapterProfile() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items_profile, parent, false);

        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Code to populate recycler view with posts
        AppPost post = posts.get(position);

        txtPostTitle.setText(post.getTitle());
        if(post.getPostType() == PostType.WEIGHT) {
            String weight = (SharedPrefsHelper.getImperial(context)) ? convertToImperialLbs(post.getPostValue()) : UnitsHelper.formatMetricWeight(post.getPostValue());
            txtPostProgress.setText("Weight Progress: " + weight);
        }else{
            txtPostProgress.setText("Footsteps: "+post.getPostValue());
        }
        txtPostBody.setText(post.getPostBody());
        if (post.getPostImageURI() != null) {
            Picasso.get().load(posts.get(position).getPostImageURI()).into(imgPostImage);
        } else {
            imgPostImage.setVisibility(View.GONE);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateStr = formatter.format(post.getDate());
        txtPostDate.setText(dateStr);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parentLayout);
            txtPostTitle = itemView.findViewById(R.id.txtPostTitle);
            txtPostProgress = itemView.findViewById(R.id.txtPostProgress);
            txtPostBody = itemView.findViewById(R.id.txtPostBody);
            imgPostImage = itemView.findViewById(R.id.imgPostImage);
            txtPostDate = itemView.findViewById(R.id.txtPostDate);
        }

    }
}
