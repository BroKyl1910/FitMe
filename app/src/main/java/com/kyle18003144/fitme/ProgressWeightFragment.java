package com.kyle18003144.fitme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;


public class ProgressWeightFragment extends Fragment {

    public ProgressWeightFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_progress_weight, container, false);

        //Add listener to open create new post window
        LinearLayout lytNewPost = rootView.findViewById(R.id.lyt_new_post);
        lytNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), NewPostActivity.class);
                startActivity(in);
            }
        });


        // Get user weights from DB
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Post");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<AppPost> posts = new ArrayList<>();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    AppPost post = child.getValue(AppPost.class);
                    if(post.getEmail().equals(userEmail) && post.getPostType()==PostType.WEIGHT){
                        posts.add(post);
                    }
                }

                GraphView graphView = rootView.findViewById(R.id.grphWeight);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                LineGraphSeries<DataPoint> goal = new LineGraphSeries<>();
                for(int i = 0; i < posts.size(); i++){
                    series.appendData(new DataPoint(i+1, posts.get(i).getPostValue()), true, posts.size());
                    goal.appendData(new DataPoint(i+1, 50), true, posts.size());
                }
                graphView.addSeries(series);
                graphView.addSeries(goal);

                series.setColor(Color.CYAN);
                series.setTitle("Weight");
                series.setDrawDataPoints(true);
                series.setThickness(2);

                goal.setColor(Color.GREEN);
                goal.setThickness(2);
                goal.setTitle("Goal");

                graphView.getLegendRenderer().setVisible(true);
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                graphView.getLegendRenderer().setSpacing(16);

                GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
                gridLabelRenderer.setGridStyle(GridLabelRenderer.GridStyle.NONE);
                gridLabelRenderer.setHorizontalLabelsVisible(false);
                gridLabelRenderer.setLabelFormatter(new DefaultLabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            // show normal x values
                            return super.formatLabel(value, isValueX);
                        } else {
                            // show currency for y values
                            return super.formatLabel(value, isValueX) + " kg";
                        }
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });



        return rootView;
    }


}
