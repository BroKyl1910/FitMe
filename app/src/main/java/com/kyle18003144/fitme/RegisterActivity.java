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

public class RegisterActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    EditText edtConfirmPassword;
    Button btnNext;
    Button btnCancel;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnNext = findViewById(R.id.btnNext);
        btnCancel = findViewById(R.id.btnCancel);

        firebaseAuth = FirebaseAuth.getInstance();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsValid()) {
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getBaseContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private boolean fieldsValid() {
        String password;
        String confirmPassword;

        password = edtPassword.getText().toString();
        confirmPassword = edtConfirmPassword.getText().toString();

        if (edtEmail.getText().toString().equals("") || edtPassword.getText().toString().equals("") || edtConfirmPassword.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "Please complete all fields", Toast.LENGTH_LONG).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(getBaseContext(), "Password must be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(getBaseContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
