package com.kyle18003144.fitme;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


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
                            if (post.getEmail().equals(userEmail) && post.getPostType() == PostType.WEIGHT) {
                                posts.add(post);
                            }
                        }

                        boolean isImperial = SharedPrefsHelper.getImperial(rootView.getContext());
//                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                        LineChart lineChart = rootView.findViewById(R.id.grphWeight);
                        if (!posts.isEmpty()) {
                            lineChart.invalidate();
                            ArrayList<Entry> series = new ArrayList<>();
                            ArrayList<Entry> goal = new ArrayList<>();

                            //Display goal and weights. First weight should be their starting weight
                            double startingWeight = ((isImperial) ? UnitsHelper.convertToImperialWeight(finalUser.getWeight()) : finalUser.getWeight());
                            series.add(new Entry(0, (float) startingWeight));

                            double goalWeight = (isImperial) ? UnitsHelper.convertToImperialWeight(finalUser.getWeightGoal()) : finalUser.getWeightGoal();
                            goal.add(new Entry(0, (float) goalWeight));

                            for (int i = 0; i < posts.size(); i++) {
                                double weight = (isImperial) ? UnitsHelper.convertToImperialWeight(posts.get(i).getPostValue()) : posts.get(i).getPostValue();
                                series.add(new Entry(i + 1, (float) weight));
                            }
                            goal.add(new Entry(posts.size(), (float) goalWeight));

                            LineDataSet weightSet;
                            LineDataSet goalSet;

                            weightSet = new LineDataSet(series, "Weight");
                            weightSet.setDrawIcons(true);
                            weightSet.enableDashedLine(10f, 5f, 0f);
                            weightSet.enableDashedHighlightLine(10f, 5f, 0f);
                            weightSet.setColor(Color.WHITE);
                            weightSet.setCircleColor(Color.WHITE);
                            weightSet.setLineWidth(1f);
                            weightSet.setCircleRadius(3f);
                            weightSet.setDrawCircleHole(true);
                            weightSet.setValueTextSize(9f);
                            weightSet.setValueTextColor(Color.WHITE);
                            weightSet.setDrawFilled(true);
                            weightSet.setFormLineWidth(1f);
                            weightSet.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                            weightSet.setFormSize(15.f);

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
                            dataSets.add(weightSet);
                            dataSets.add(goalSet);
                            LineData data = new LineData(dataSets);
                            lineChart.setData(data);
                        }
                        else{
                            lineChart.setNoDataText("No weight data to graph");
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
