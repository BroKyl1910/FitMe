package com.kyle18003144.fitme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.Date;

public class NewPostActivity extends AppCompatActivity implements IPickResult {

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    TextView txtCancel;
    ImageView imgUpload;
    ImageView imgUserImage;
    Button btnSave;
    ProgressBar prgLoading;

    EditText edtTitle;
    EditText edtBody;
    EditText edtWeight;


    boolean hasImage = false;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        txtCancel = findViewById(R.id.txtCancel);
        imgUpload = findViewById(R.id.imgUpload);
        imgUserImage = findViewById(R.id.imgUserImage);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtBody = findViewById(R.id.edtBody);
        edtWeight = findViewById(R.id.edtWeight);
        prgLoading = findViewById(R.id.prgLoading);

        if(SharedPrefsHelper.getImperial(getBaseContext())){
            edtWeight.setHint("New Weight (lbs)");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Post");
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewPostActivity.this, MainFragmentHostActivity.class);
                i.putExtra("fragment", R.id.nav_progress);
                startActivity(i);
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(getSupportFragmentManager());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String body = edtBody.getText().toString();
                String strWeight = edtWeight.getText().toString();
                int weight = (strWeight.equals(""))?0:Integer.parseInt(strWeight);

                if(title.equals("") || body.equals("") || weight<=0){
                    Toast.makeText(NewPostActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Set up data to push to DB
                final AppPost post = new AppPost();
                post.setEmail(firebaseAuth.getCurrentUser().getEmail());
                post.setTitle(title);
                post.setPostBody(body);
                post.setPostType(PostType.WEIGHT);
                post.setPostValue((SharedPrefsHelper.getImperial(getBaseContext()))? UnitsHelper.convertToMetricWeight(weight):weight);
                post.setContainerID(databaseReference.push().getKey());
                post.setDate(new Date());

//                new CreatePost(post).execute();
                btnSave.setVisibility(View.GONE);
                prgLoading.setVisibility(View.VISIBLE);

                if(hasImage){
                    final String imageName = System.currentTimeMillis()+"";
                    storageReference = storageReference.child("Posts").child(imageName+".jpg");
                    storageReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            post.setPostImageURI(uri.toString());
                                            Toast.makeText(NewPostActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();

                                            databaseReference.child(post.getContainerID()).setValue(post);
                                            Toast.makeText(NewPostActivity.this, "Post Saved", Toast.LENGTH_SHORT).show();

                                            btnSave.setVisibility(View.VISIBLE);
                                            prgLoading.setVisibility(View.GONE);

                                            Intent i = new Intent(NewPostActivity.this, MainFragmentHostActivity.class);
                                            i.putExtra("fragment", R.id.nav_profile);
                                            startActivity(i);

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.d("downloadURI", exception.toString());

                                    btnSave.setVisibility(View.VISIBLE);
                                    prgLoading.setVisibility(View.GONE);
                                }
                            });
                } else{
                    databaseReference.child(post.getContainerID()).setValue(post);
                    Toast.makeText(NewPostActivity.this, "Post Saved", Toast.LENGTH_SHORT).show();

                    btnSave.setVisibility(View.VISIBLE);
                    prgLoading.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            imgUserImage.setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
            imgUserImage.setVisibility(View.VISIBLE);
            imgUserImage.setImageBitmap(r.getBitmap());

            hasImage = true;
            imageUri = r.getUri();
            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
