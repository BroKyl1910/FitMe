package com.kyle18003144.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText edtFirstName;
    EditText edtSurname;
    EditText edtHeight;
    EditText edtIdealWeight;
    EditText edtIdealFootsteps;

    TextView txtCancel;
    TextView txtWeightGoalLabel;
    TextView txtHeightLabel;
    Button btnSave;

    boolean isImperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        edtFirstName = findViewById(R.id.edtFirstName);
        edtSurname = findViewById(R.id.edtSurname);
        edtHeight = findViewById(R.id.edtHeight);
        edtIdealWeight = findViewById(R.id.edtIdealWeight);
        edtIdealFootsteps = findViewById(R.id.edtIdealFootsteps);
        txtCancel = findViewById(R.id.txtCancel);
        txtWeightGoalLabel = findViewById(R.id.txtWeightGoalLabel);
        txtHeightLabel = findViewById(R.id.txtHeightLabel);
        btnSave = findViewById(R.id.btnSave);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("User");

        isImperial = SharedPrefsHelper.getImperial(getBaseContext());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Set up user object to be edited
                AppUser appUser = new AppUser();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    appUser = child.getValue(AppUser.class);
                    if(appUser.getEmail().equals(user.getEmail())){
                        break;
                    }
                }

                edtFirstName.setText(appUser.getFirstName());
                edtSurname.setText(appUser.getSurname());
                String heightUnit = (isImperial)? "(inches)":"(cm)";
                txtHeightLabel.setText("Height "+heightUnit);
                edtHeight.setText((isImperial)? UnitsHelper.convertToImperialHeight(appUser.getHeight())+"":(int)appUser.getHeight()+"");
                edtIdealFootsteps.setText(((int)appUser.getFootstepsGoal())+"");
                String weightUnit = (isImperial)? "(lbs)":"(kg)";
                txtWeightGoalLabel.setText("Weight Goal "+weightUnit);
                edtIdealWeight.setText((isImperial)? UnitsHelper.convertToImperialWeight(appUser.getWeightGoal())+"":(int)appUser.getWeightGoal()+"");

                final AppUser finalAppUser = appUser;

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(fieldsValid()){
                            finalAppUser.setFirstName(edtFirstName.getText().toString());
                            finalAppUser.setSurname(edtSurname.getText().toString());
                            finalAppUser.setFootstepsGoal(Integer.parseInt(edtIdealFootsteps.getText().toString()));
                            int weight = ((isImperial)? UnitsHelper.convertToMetricWeight(Integer.parseInt(edtIdealWeight.getText().toString())):Integer.parseInt(edtIdealWeight.getText().toString()));
                            finalAppUser.setWeightGoal(weight);
                            int height = ((isImperial)? UnitsHelper.convertToMetricHeight(Integer.parseInt(edtHeight.getText().toString())):Integer.parseInt(edtHeight.getText().toString()));
                            finalAppUser.setHeight(height);
                            databaseReference.child(finalAppUser.getContainerID()).setValue(finalAppUser);

                            Intent i = new Intent(EditProfileActivity.this, MainFragmentHostActivity.class);
                            i.putExtra("fragment", R.id.nav_profile);
                            startActivity(i);
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, MainFragmentHostActivity.class);
                i.putExtra("fragment", R.id.nav_profile);
                startActivity(i);
            }
        });
    }

    private boolean fieldsValid() {

        if (edtFirstName.getText().toString().equals("") || edtSurname.getText().toString().equals("") || edtIdealWeight.getText().toString().equals("")|| edtIdealFootsteps.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Please complete all fields", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
