package com.kyle18003144.fitme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NewPostActivity extends AppCompatActivity {

    TextView txtCancel;
    ImageView imgUpload;
    ImageView imgUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        txtCancel = findViewById(R.id.txtCancel);
        imgUpload = findViewById(R.id.imgUpload);
        imgUserImage = findViewById(R.id.imgUserImage);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewPostActivity.this, MainFragmentHostActivity.class);
                i.putExtra("fragment", "Progress");
                startActivity(i);
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);

            }
        });
    }
}
