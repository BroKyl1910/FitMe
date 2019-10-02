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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnNext = findViewById(R.id.btnNext);
        btnCancel = findViewById(R.id.btnCancel);


        Intent i = getIntent();
        if(i.hasExtra("email")){
           edtEmail.setText(i.getStringExtra("email"));
           edtPassword.setText(i.getStringExtra("password"));
        }


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, SplashActivity.class);
                startActivity(i);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fieldsValid()) {
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();

                    Intent i = new Intent(RegisterActivity.this, RegisterPersonalActivity.class);
                    i.putExtra("email", email);
                    i.putExtra("password", password);

                    startActivity(i);
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
