package com.kyle18003144.fitme;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ProfileFragment extends Fragment {

    TextView txtName;
    TextView txtEmail;
    TextView txtStepGoal;
    TextView txtWeightGoal;

    boolean isImperial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        isImperial = SharedPrefsHelper.getImperial(rootView.getContext());

        txtName = rootView.findViewById(R.id.txtName);
        txtEmail = rootView.findViewById(R.id.edtEmail);
        txtStepGoal = rootView.findViewById(R.id.txtStepGoal);
        txtWeightGoal = rootView.findViewById(R.id.txtWeightGoal);
        Log.d("UserFound", "Create");

        //Added from laptop

        //Get user
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("UserFound", "Email: "+userEmail);
        DatabaseReference userReference = database.getReference("User");

        //Get basic user info

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

                txtName.setText(user.getFirstName()+" "+user.getSurname());
                txtEmail.setText(user.getEmail());
                txtStepGoal.setText(((isImperial)?convertToImperial(user.getFootstepsGoal()):user.getFootstepsGoal())+"");
                txtWeightGoal.setText(((isImperial)?convertToImperial(user.getWeightGoal()):user.getWeightGoal())+"");


                DatabaseReference postReference = database.getReference("Post");
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
