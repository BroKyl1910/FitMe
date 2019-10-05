package com.kyle18003144.fitme;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    Switch swtchImperial;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        swtchImperial = rootView.findViewById(R.id.swtchImperial);

        //Set switch position according to setting
        boolean useImperial = SharedPrefsHelper.getImperial(rootView.getContext());
        swtchImperial.setChecked(useImperial);

        //Change setting when switch is checked or unchecked
        swtchImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swtchImperial.isChecked()){
                    SharedPrefsHelper.setImperial(rootView.getContext(), true);
                } else{
                    SharedPrefsHelper.setImperial(rootView.getContext(), false);
                }
            }
        });

        return rootView;
    }
}
