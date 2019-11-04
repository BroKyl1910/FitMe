package com.kyle18003144.fitme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class ProgressFootstepsFragment extends Fragment {

    public ProgressFootstepsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_progress_footsteps, container, false);

        // Get user weights from DB
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userReference = database.getReference("User");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUser user = new AppUser();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user = child.getValue(AppUser.class);
                    if (user.getEmail().equals(userEmail)) {
                        break;
                    }
                }


                DatabaseReference postReference = database.getReference("Post");

                final AppUser finalUser = user;
                postReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<AppPost> posts = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            AppPost post = child.getValue(AppPost.class);
                            if (post.getEmail().equals(userEmail) && post.getPostType() == PostType.FOOTSTEPS) {
                                posts.add(post);
                            }
                        }

                        if (!posts.isEmpty()) {
                            LineChart lineChart = rootView.findViewById(R.id.grphFootsteps);
                            lineChart.setTouchEnabled(true);
                            lineChart.setPinchZoom(true);
                            lineChart.invalidate();


                            ArrayList<Entry> series = new ArrayList<>();
                            ArrayList<Entry> goal = new ArrayList<>();

                            //Display goal and weights. First weight should be their starting weight
                            for (int i = 0; i < posts.size(); i++) {
                                series.add(new Entry(i + 1, (float) posts.get(i).getPostValue()));
                                goal.add(new Entry(i + 1, finalUser.getFootstepsGoal()));
                            }

                            LineDataSet footstepsSet;
                            LineDataSet goalSet;

                            footstepsSet = new LineDataSet(series, "Footsteps");
                            footstepsSet.setDrawIcons(true);
                            footstepsSet.enableDashedLine(10f, 5f, 0f);
                            footstepsSet.enableDashedHighlightLine(10f, 5f, 0f);
                            footstepsSet.setColor(Color.WHITE);
                            footstepsSet.setCircleColor(Color.WHITE);
                            footstepsSet.setLineWidth(1f);
                            footstepsSet.setCircleRadius(3f);
                            footstepsSet.setDrawCircleHole(true);
                            footstepsSet.setValueTextSize(9f);
                            footstepsSet.setValueTextColor(Color.WHITE);
                            footstepsSet.setDrawFilled(true);
                            footstepsSet.setFormLineWidth(1f);
                            footstepsSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                            footstepsSet.setFormSize(15.f);

                            goalSet = new LineDataSet(goal, "Goal");
                            goalSet.setDrawIcons(false);
                            goalSet.enableDashedLine(10f, 5f, 0f);
                            goalSet.enableDashedHighlightLine(10f, 5f, 0f);
                            goalSet.setColor(Color.YELLOW);
                            goalSet.setLineWidth(1f);
                            goalSet.setCircleRadius(1f);
                            goalSet.setDrawCircleHole(true);
                            goalSet.setValueTextSize(9f);
                            goalSet.setValueTextColor(Color.YELLOW);
                            goalSet.setDrawFilled(false);
                            goalSet.setFormLineWidth(1f);
                            goalSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                            goalSet.setFormSize(15.f);


                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setValueFormatter(new IAxisValueFormatter() {
                                @Override
                                public String getFormattedValue(float value, AxisBase axis) {
                                    long millis = TimeUnit.DAYS.toMillis((long) value);
                                    Date d = new Date(Float.valueOf(value).longValue());
                                    String date = new SimpleDateFormat("MM-dd-yyyy").format(millis);
                                    return date;
                                }
                            });


                            lineChart.getXAxis().setTextColor(Color.WHITE);
                            lineChart.getAxisLeft().setTextColor(Color.WHITE);
                            lineChart.getAxisRight().setTextColor(Color.WHITE);
                            lineChart.getLegend().setTextColor(Color.WHITE);
                            lineChart.getDescription().setEnabled(false);

                            lineChart.getXAxis().setEnabled(false);
                            lineChart.getAxisRight().setEnabled(false);

                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(footstepsSet);
                            dataSets.add(goalSet);
                            LineData data = new LineData(dataSets);
                            lineChart.setData(data);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("TAG", "Failed to read value.", error.toException());
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
