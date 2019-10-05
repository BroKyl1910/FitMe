package com.kyle18003144.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterPersonalActivity extends AppCompatActivity {

    EditText edtFirstName;
    EditText edtSurname;
    EditText edtHeight;
    EditText edtWeight;
    EditText edtIdealWeight;
    EditText edtIdealFootsteps;

    Button btnBack;
    Button btnFinish;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_personal);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        intent = getIntent();

        edtFirstName = findViewById(R.id.edtFirstName);
        edtSurname = findViewById(R.id.edtSurname);
        edtHeight = findViewById(R.id.edtHeight);
        edtWeight = findViewById(R.id.edtWeight);
        edtIdealWeight = findViewById(R.id.edtIdealWeight);
        edtIdealFootsteps = findViewById(R.id.edtIdealFootsteps);
        btnBack = findViewById(R.id.btnBack);
        btnFinish = findViewById(R.id.btnFinish);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPersonalActivity.this, RegisterActivity.class);
                i.putExtra("email", intent.getStringExtra("email"));
                i.putExtra("password", intent.getStringExtra("password"));

                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsValid()) {
                    String email = intent.getStringExtra("email");
                    String password = intent.getStringExtra("password");

                    //Register user in Firebase Auth and then the database
                    registerUser(email, password);

                }
            }
        });

    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_LONG).show();

                            //User is in Auth, so push to database
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String containerID = databaseReference.push().getKey();
                            AppUser appUser = new AppUser(user.getEmail(), edtFirstName.getText().toString().trim(), edtSurname.getText().toString().trim(), Integer.parseInt(edtHeight.getText().toString()), Integer.parseInt(edtWeight.getText().toString()),Integer.parseInt(edtIdealFootsteps.getText().toString()), Integer.parseInt(edtIdealWeight.getText().toString()), containerID);

                            databaseReference.child(containerID).setValue(appUser);
                            Toast.makeText(getBaseContext(),"Upload successful", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(RegisterPersonalActivity.this, MainFragmentHostActivity.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private boolean fieldsValid() {

        if (edtFirstName.getText().toString().equals("") || edtSurname.getText().toString().equals("") || edtHeight.getText().toString().equals("") || edtWeight.getText().toString().equals("") || edtIdealWeight.getText().toString().equals("")|| edtIdealFootsteps.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Please complete all fields", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
