package com.kyle18003144.fitme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class FootstepsBroadcastReceiver extends BroadcastReceiver {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    public void onReceive(Context context, Intent intent) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Post");

        int numSteps = SharedPrefsHelper.getFootsteps(context);

        Toast.makeText(context, "Footsteps posted: " + numSteps, Toast.LENGTH_LONG).show();

        String title = "Footsteps";
        String body = "";

        // Set up data to push to DB
        final AppPost post = new AppPost();
        post.setEmail(firebaseAuth.getCurrentUser().getEmail());
        post.setTitle(title);
        post.setPostBody(body);
        post.setPostType(PostType.FOOTSTEPS);
        post.setPostValue(numSteps);
        post.setContainerID(databaseReference.push().getKey());
        post.setDate(new Date());

        databaseReference.child(post.getContainerID()).setValue(post);

        Toast.makeText(context, "Footsteps Saved", Toast.LENGTH_SHORT).show();
    }

}
