package com.kyle18003144.fitme;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ProgressWeightFragment extends Fragment {

    public ProgressWeightFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_progress_weight, container, false);

        LinearLayout lytNewPost = rootView.findViewById(R.id.lyt_new_post);
        lytNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getContext(), NewPostActivity.class);
                startActivity(in);
            }
        });
        return rootView;
    }
}
