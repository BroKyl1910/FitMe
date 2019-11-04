package com.kyle18003144.fitme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FootstepsBroadcastReceiver extends BroadcastReceiver {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_SHUTDOWN.equals(intent.getAction())){
            //Shutdown event
        } else{
            //Alarm
        }
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Post");

        int numSteps = SharedPrefsHelper.getTodayFootsteps(context) - SharedPrefsHelper.getPreviousFootsteps(context);

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
        post.setDate(SharedPrefsHelper.getDate(context));

        databaseReference.child(post.getContainerID()).setValue(post);

        SharedPrefsHelper.setPreviousFootsteps(context, numSteps);
        Toast.makeText(context, "Footsteps Saved", Toast.LENGTH_SHORT).show();

        BroadcastsHelper.scheduleRepeatingFootstepUpload(context);
    }

}
