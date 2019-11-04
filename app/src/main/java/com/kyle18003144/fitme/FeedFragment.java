package com.kyle18003144.fitme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.zip.Inflater;


public class FeedFragment extends Fragment{

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = rootView.findViewById(R.id.feedRecyclerView);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference("User");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, String> emailNameHashMap = new HashMap<>();
                AppUser currentUser = new AppUser();
                // Find user in user db
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AppUser user = child.getValue(AppUser.class);
                    emailNameHashMap.put(user.getEmail(), user.getFirstName()+" "+ user.getSurname());
                    if (user.getEmail().equals(userEmail)) {
                        currentUser = user;
                    }
                }

                final ArrayList<String> following = currentUser.getFollowing();
                following.add(currentUser.getEmail()); //Add user's email to make searches easier
                DatabaseReference postReference = database.getReference("Post");

                postReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<AppPost> allPosts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            AppPost post = child.getValue(AppPost.class);
                            if (following.contains(post.getEmail())) {
                                allPosts.add(post);
                            }
                        }

                        Collections.sort(allPosts);

                        recyclerView.setAdapter(new RecyclerViewAdapterFeed(getContext(), allPosts, emailNameHashMap));
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
