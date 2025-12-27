package com.bb.spamfolder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.adapters.MessageAdapter;
import com.bb.spamfolder.models.Message;
import com.bb.spamfolder.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        setupViews(view);
        return view;
    }

    private void setupViews(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext()));
        MessageAdapter adapter = new MessageAdapter(getDummyMessages());

        adapter.setOnItemClickListener(message -> {
            // Open ChatDetailFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, ChatDetailFragment.newInstance(message))
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView.setAdapter(adapter);
    }

    private List<Message> getDummyMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(
                "Master 1",
                "I Just Delivered #12456541427 to the buyer...",
                "2:34 pm",
                0,
                true,
                true,
                R.drawable.profile_male_1,
                R.drawable.ic_delivered_blue
        ));
        messages.add(new Message(
                "Dispatcher 123",
                "I Just Delivered #12456541427 to the buyer...",
                "Yesterday",
                0,
                false,
                true,
                R.drawable.profile_male_2,
                R.drawable.ic_delivered_black
        ));
        messages.add(new Message(
                "Dispatcher 456",
                "I Just Delivered #12456541427 to the buyer...",
                "12/31/24",
                0,
                false,
                true,
                R.drawable.profile_female_5,
                R.drawable.ic_delivered_tick
        ));
        messages.add(new Message(
                "Dispatcher 789",
                "Call me when you reach block 12345 on 58 for me.",
                "12/29/24",
                365,
                true,
                false,
                R.drawable.profile_male_4,
                0  // No delivery icon needed
        ));
        // Add more dummy messages as needed
        return messages;
    }
} 