package com.kyle18003144.fitme;

import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.widget.LinearLayout.VERTICAL;


public class ProfileFragment extends Fragment {

    TextView txtName;
    TextView txtEmail;
    TextView txtStepGoal;
    TextView txtWeightGoal;
    LinearLayout lytCardHost;

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
        lytCardHost = rootView.findViewById(R.id.lytCardHost);
        Log.d("UserFound", "Create");

        //Get user
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("UserFound", "Email: "+userEmail);
        DatabaseReference userReference = database.getReference("User");
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();


        //Get user posts

        //Get picture from post

        //Add post to Layout

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUser user = new AppUser();
                // Find user in user db
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    user = child.getValue(AppUser.class);
                    if(user.getEmail().equals(userEmail)){
                        Log.d("UserFound", "USER FOUND");
                        Log.d("UserFound", userEmail);
                        break;
                    }
                }

                //Set basic user info
                txtName.setText(user.getFirstName()+" "+user.getSurname());
                txtEmail.setText(user.getEmail());
                txtStepGoal.setText(user.getFootstepsGoal()+"");
                txtWeightGoal.setText(((isImperial)?convertToImperial(user.getWeightGoal()):user.getWeightGoal())+"");

                DatabaseReference postReference = database.getReference("Post");

                postReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<AppPost> posts = new ArrayList<>();
                        for(DataSnapshot child: dataSnapshot.getChildren()){
                            AppPost post = child.getValue(AppPost.class);
                            if(post.getEmail().equals(userEmail)){
                                posts.add(post);
                            }
                        }

                        for (AppPost post : posts){
                            LinearLayout postLayout = new LinearLayout(rootView.getContext());
                            postLayout.setOrientation(VERTICAL);
                            postLayout.setBackgroundResource(R.color.colorPrimary);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.bottomMargin = 16;
                            int padding = (int) rootView.getResources().getDimension(R.dimen.appbar_padding);
                            postLayout.setPadding(padding,padding,padding,padding);
                            postLayout.setLayoutParams(layoutParams);

                            TextView txtTitle = new TextView(rootView.getContext());
                            txtTitle.setText(post.getTitle());
                            ViewGroup.LayoutParams txtTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtTitle.setLayoutParams(txtTitleParams);
                            txtTitle.setTextAppearance(rootView.getContext(), R.style.TextAppearance_MaterialComponents_Headline6);
                            postLayout.addView(txtTitle);

                            TextView txtProgress = new TextView(rootView.getContext());
                            txtProgress.setText("Weight Progress: "+((isImperial)?convertToImperial(post.getPostValue())+" lbs":post.getPostValue()+" kg"));
                            LinearLayout.LayoutParams txtProgressParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtProgressParams.topMargin = 8;
                            txtProgress.setLayoutParams(txtProgressParams);
                            txtProgress.setTextAppearance(rootView.getContext(), R.style.TextAppearance_MaterialComponents_Body1);
                            postLayout.addView(txtProgress);

                            TextView txtBody = new TextView(rootView.getContext());
                            txtBody.setText(post.getPostBody());
                            LinearLayout.LayoutParams txtBodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtBodyParams.topMargin = 8;
                            txtBody.setLayoutParams(txtBodyParams);
                            txtBody.setTextAppearance(rootView.getContext(), R.style.TextAppearance_MaterialComponents_Body2);
                            postLayout.addView(txtBody);

                            if(post.getPostImageURI() != null){
                                final ImageView imgImage = new ImageView(rootView.getContext());
                                LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                                imgParams.topMargin = 8;
                                imgParams.gravity = Gravity.CENTER;
                                imgImage.setLayoutParams(imgParams);

                                Picasso.get().load(post.getPostImageURI()).into(imgImage);
                                postLayout.addView(imgImage);
                            }


                            lytCardHost.addView(postLayout);
                        }
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

    private double convertToImperial(double metric){
        return metric * 2.20462;
    }
}
