package com.kyle18003144.fitme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import static android.widget.LinearLayout.VERTICAL;
import static com.kyle18003144.fitme.UnitsHelper.convertToImperialHeightIn;
import static com.kyle18003144.fitme.UnitsHelper.convertToImperialLbs;


public class ProfileFragment extends Fragment {

    TextView txtName;
    TextView txtEmail;
    TextView txtStepGoal;
    TextView txtWeightGoal;
    TextView txtHeight;
    TextView txtBMI;
    LinearLayout lyoutLabels;
    RecyclerView recyclerView;
    ImageView imgEdit;

    boolean isImperial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        isImperial = SharedPrefsHelper.getImperial(rootView.getContext());

        txtName = rootView.findViewById(R.id.txtName);
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtStepGoal = rootView.findViewById(R.id.txtStepGoal);
        txtWeightGoal = rootView.findViewById(R.id.txtWeightGoal);
        txtHeight = rootView.findViewById(R.id.txtHeight);
        txtBMI = rootView.findViewById(R.id.txtBMI);
        imgEdit = rootView.findViewById(R.id.imgEdit);
        lyoutLabels = rootView.findViewById(R.id.lyoutLabels);
        recyclerView = rootView.findViewById(R.id.profileRecyclerView);

        //Get user
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("UserFound", "Email: " + userEmail);
        DatabaseReference userReference = database.getReference("User");

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUser user = new AppUser();
                // Find user in user db
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user = child.getValue(AppUser.class);
                    if (user.getEmail().equals(userEmail)) {
                        break;
                    }
                }

                //Set basic user info
                txtName.setText(user.getFirstName() + " " + user.getSurname());
                txtEmail.setText(user.getEmail());
                txtStepGoal.setText(user.getFootstepsGoal() + "");
                txtWeightGoal.setText(((isImperial) ? convertToImperialLbs(user.getWeightGoal()) : UnitsHelper.formatMetricWeight(user.getWeightGoal())));
                txtHeight.setText(((isImperial) ? convertToImperialHeightIn(user.getHeight()) : UnitsHelper.formatMetricHeight(user.getHeight())));
                lyoutLabels.setVisibility(View.VISIBLE);

                DatabaseReference postReference = database.getReference("Post");

                final AppUser finalUser = user;
                postReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<AppPost> posts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            AppPost post = child.getValue(AppPost.class);
                            if (post.getEmail().equals(userEmail)) {
                                posts.add(post);
                            }
                        }
                        //When footsteps are saved, need separate loop to get only weight posts
                        Collections.reverse(posts);

                        if (posts.size() == 0) {
                            //Calculate BMI based on initial height but latest weight
                            txtBMI.setText(calcBMI(finalUser.getWeight(), finalUser.getHeight()) + "");
                        } else {
                            //Show posts in the order of newest first
                            for(AppPost post:posts){
                                if(post.getPostType()==PostType.WEIGHT){
                                    txtBMI.setText(calcBMI(post.getPostValue(), finalUser.getHeight()) + "");
                                }
                            }

                        }

                        recyclerView.setAdapter(new RecyclerViewAdapterProfile(getContext(), posts));
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

    private int calcBMI(double weight, double height) {
        int kg = (int) weight;
        height /= 100;
        return (int) (kg / (height * height));
    }
}
