package com.bb.spamfolder.fragments;

import static com.bb.spamfolder.utils.Constant.KEY_AVATAR;
import static com.bb.spamfolder.utils.Constant.KEY_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Message;

public class ContactInfoFragment extends Fragment {

    public static ContactInfoFragment newInstance(Message message) {
        ContactInfoFragment fragment = new ContactInfoFragment();
        Bundle args = new Bundle();
        args.putString(KEY_NAME, message.name());
        args.putInt(KEY_AVATAR, message.drawableImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        ImageView ivBack = view.findViewById(R.id.ivBack);
        ImageView ivProfilePic = view.findViewById(R.id.ivProfilePic);
        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvClearChat = view.findViewById(R.id.tvClearChat);
        TextView tvDeleteChat = view.findViewById(R.id.tvDeleteChat);

        if (getArguments() != null) {
            tvName.setText(getArguments().getString(KEY_NAME));
            ivProfilePic.setImageResource(getArguments().getInt(KEY_AVATAR));
        }

        ivBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        tvClearChat.setOnClickListener(v -> {
            // Handle clear chat
        });

        tvDeleteChat.setOnClickListener(v -> {
            // Handle delete chat
        });
    }
} 