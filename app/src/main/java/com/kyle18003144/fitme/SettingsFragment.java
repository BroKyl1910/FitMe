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
    Switch swtchRemember;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        swtchImperial = rootView.findViewById(R.id.swtchImperial);
        swtchRemember = rootView.findViewById(R.id.swtchRemember);

        //Set switch position according to setting
        boolean useImperial = SharedPrefsHelper.getImperial(rootView.getContext());
        swtchImperial.setChecked(useImperial);

        boolean remember = SharedPrefsHelper.getRememberMe(rootView.getContext());
        swtchRemember.setChecked(remember);

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

        //Change setting when switch is checked or unchecked
        swtchRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(swtchRemember.isChecked()){
                    SharedPrefsHelper.setRememberMe(rootView.getContext(), true);
                } else{
                    SharedPrefsHelper.setRememberMe(rootView.getContext(), false);
                }
            }
        });

        return rootView;
    }
}
