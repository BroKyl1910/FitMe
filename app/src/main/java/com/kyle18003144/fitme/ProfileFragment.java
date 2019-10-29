package com.kyle18003144.fitme;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
    LinearLayout lytCardHost;
    LinearLayout lyoutLabels;

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
        lytCardHost = rootView.findViewById(R.id.lytCardHost);
        lyoutLabels = rootView.findViewById(R.id.lyoutLabels);

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
                            //Show posts in the order of newest first
                            txtBMI.setText(calcBMI(finalUser.getWeight(), finalUser.getHeight()) + "");
                        } else {
                            //Calculate BMI based on initial height but latest weight
                            txtBMI.setText(calcBMI(posts.get(0).getPostValue(), finalUser.getHeight()) + "");
                        }
                        for (AppPost post : posts) {
                            LinearLayout postLayout = new LinearLayout(rootView.getContext());
                            postLayout.setOrientation(VERTICAL);
                            postLayout.setBackgroundResource(R.color.colorPrimary);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.bottomMargin = 16;
                            int padding = (int) rootView.getResources().getDimension(R.dimen.appbar_padding);
                            postLayout.setPadding(padding, padding, padding, padding);
                            postLayout.setLayoutParams(layoutParams);

                            TextView txtTitle = new TextView(rootView.getContext());
                            txtTitle.setText(post.getTitle());
                            ViewGroup.LayoutParams txtTitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtTitle.setLayoutParams(txtTitleParams);
                            txtTitle.setTextAppearance(rootView.getContext(), R.style.TextAppearance_MaterialComponents_Headline6);
                            postLayout.addView(txtTitle);

                            TextView txtProgress = new TextView(rootView.getContext());
                            String weight = (isImperial) ? convertToImperialLbs(post.getPostValue()) : UnitsHelper.formatMetricWeight(post.getPostValue());
                            txtProgress.setText("Weight Progress: " + weight);
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

                            if (post.getPostImageURI() != null) {
                                final ImageView imgImage = new ImageView(rootView.getContext());
                                LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 500);
                                imgParams.topMargin = 8;
                                imgParams.gravity = Gravity.CENTER;
                                imgImage.setLayoutParams(imgParams);

                                Picasso.get().load(post.getPostImageURI()).into(imgImage);
                                postLayout.addView(imgImage);
                            }

                            TextView txtDate = new TextView(rootView.getContext());
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String dateStr = formatter.format(post.getDate());
                            txtDate.setText(dateStr);
                            LinearLayout.LayoutParams txtDateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            txtDateParams.topMargin = 100;
                            txtDate.setLayoutParams(txtDateParams);
                            txtDate.setTextAppearance(rootView.getContext(), R.style.TextAppearance_MaterialComponents_Body2);
                            postLayout.addView(txtDate);


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

    private int calcBMI(double weight, double height) {
        int kg = (int) weight;
        height /= 100;
        return (int) (kg / (height * height));
    }
}
