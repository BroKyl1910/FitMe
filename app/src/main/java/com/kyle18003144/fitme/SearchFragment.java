package com.kyle18003144.fitme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    EditText edtSearch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = rootView.findViewById(R.id.searchRecyclerView);
        edtSearch = rootView.findViewById(R.id.edtSearch);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userReference = database.getReference("User");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUser currentUser = new AppUser();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AppUser user = child.getValue(AppUser.class);
                    if (user.getEmail().equals(userEmail)) {
                        currentUser = user;
                        break;
                    }
                }

                final AppUser finalCurrentUser = currentUser;
                edtSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        userReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ArrayList<AppUser> users = new ArrayList<>();
                                String searchText = edtSearch.getText().toString();
                                if (!searchText.equals("")) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        AppUser user = child.getValue(AppUser.class);
                                        if (!user.getEmail().equals(finalCurrentUser.getEmail()) && (user.getEmail().contains(searchText) || user.getFirstName().contains(searchText) || user.getSurname().contains(searchText) || (user.getFirstName() + " " + user.getSurname()).contains(searchText))) {
                                            users.add(user);
                                        }
                                    }

                                }

                                recyclerView.setAdapter(new RecyclerViewAdapterSearch(getContext(), users, finalCurrentUser));
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

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
