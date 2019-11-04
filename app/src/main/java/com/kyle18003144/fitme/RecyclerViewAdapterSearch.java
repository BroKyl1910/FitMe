package com.kyle18003144.fitme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.kyle18003144.fitme.UnitsHelper.convertToImperialLbs;


public class RecyclerViewAdapterSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    LinearLayout parentLayout;
    Context context;
    ViewHolder viewHolder;
    ArrayList<AppUser> users = new ArrayList<>();
    AppUser currentUser;

    TextView txtName;
    TextView txtEmail;
    Button btnFollow;
    Button btnFollowing;


    public RecyclerViewAdapterSearch(Context context, ArrayList<AppUser> users, AppUser user) {
        this.context = context;
        this.users = users;
        this.currentUser = user;
    }

    public RecyclerViewAdapterSearch() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items_search, parent, false);

        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //Code to populate recycler view with posts
        final AppUser user = users.get(position);

        txtName.setText(user.getFirstName() + " " + user.getSurname());
        txtEmail.setText(user.getEmail());

        if (currentUser.getFollowing() == null || !currentUser.isFollowing(user.getEmail())) {
            btnFollowing.setVisibility(View.GONE);
        } else {
            btnFollow.setVisibility(View.GONE);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userReference = database.getReference("User");

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUser userToFollow = users.get(position);

                userToFollow.addFollower(currentUser.getEmail());
                currentUser.followUser(userToFollow.getEmail());

                userReference.child(userToFollow.getContainerID()).setValue(userToFollow);
                userReference.child(currentUser.getContainerID()).setValue(currentUser);
            }
        });


        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUser userToUnfollow = users.get(position);

                userToUnfollow.removeFollower(currentUser.getEmail());
                currentUser.unfollowUser(userToUnfollow.getEmail());

                userReference.child(userToUnfollow.getContainerID()).setValue(userToUnfollow);
                userReference.child(currentUser.getContainerID()).setValue(currentUser);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parentLayout);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            btnFollowing = itemView.findViewById(R.id.btnFollowing);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }

    }
}
