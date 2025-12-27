package com.bb.spamfolder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bb.spamfolder.R;
import com.bb.spamfolder.activities.LoginActivity;
import com.bb.spamfolder.utils.Screens;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button btnLogin = view.findViewById(R.id.btnLogout);

        // Set click listeners
        btnLogin.setOnClickListener(v -> handleLogin());
        return view;
    }

    private void handleLogin() {
        Screens.showClearTopScreen(requireActivity(), LoginActivity.class);
    }
} 