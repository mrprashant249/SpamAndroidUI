package com.bb.spamfolder.fragments;

import static com.bb.spamfolder.utils.Constant.KEY_AVATAR;
import static com.bb.spamfolder.utils.Constant.KEY_NAME;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.adapters.ChatAdapter;
import com.bb.spamfolder.models.Chat;
import com.bb.spamfolder.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private View normalHeader;
    private View searchLayout;

    public static ChatDetailFragment newInstance(Message message) {
        ChatDetailFragment fragment = new ChatDetailFragment();
        Bundle args = new Bundle();
        // Add message details as arguments
        args.putString(KEY_NAME, message.name());
        args.putInt(KEY_AVATAR, message.drawableImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);

        // Add click listener for more options menu
        ImageView ivMore = view.findViewById(R.id.ivMore);
        ivMore.setOnClickListener(this::showPopupMenu);
    }

    private void setupViews(View view) {
        // Setup header
        ImageView ivBack = view.findViewById(R.id.ivBack);
        ImageView ivAvatar = view.findViewById(R.id.ivAvatar);
        TextView tvName = view.findViewById(R.id.tvName);
        View nameLayout = view.findViewById(R.id.nameLayout);

        // Set data from arguments
        if (getArguments() != null) {
            tvName.setText(getArguments().getString(KEY_NAME));
            ivAvatar.setImageResource(getArguments().getInt(KEY_AVATAR));
        }

        ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
        nameLayout.setOnClickListener(view1 -> openContactInfo());

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);  // Makes items stack from bottom
        layoutManager.setReverseLayout(false); // Keep chronological order
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(getDummyChats());
        recyclerView.setAdapter(adapter);

        // Optional: Scroll to bottom when keyboard appears
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.postDelayed(() ->
                        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1), 100);
            }
        });

        // Initialize search views
        normalHeader = view.findViewById(R.id.normalHeader);  // Add this id to your existing header layout
        searchLayout = view.findViewById(R.id.searchLayout);
        ImageView ivSearch = view.findViewById(R.id.ivSearch);
        ImageView ivBackSearch = view.findViewById(R.id.ivBackSearch);

        // Setup search click listeners
        ivSearch.setOnClickListener(v -> {
            normalHeader.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
        });

        ivBackSearch.setOnClickListener(v -> {
            searchLayout.setVisibility(View.GONE);
            normalHeader.setVisibility(View.VISIBLE);
        });
    }

    private void openContactInfo() {
        // Get the current message object
        assert getArguments() != null;
        Message currentMessage = new Message(
                getArguments().getString(KEY_NAME, ""),
                "",
                "",
                0,
                true,
                false,
                getArguments().getInt(KEY_AVATAR),
                0
        );

        // Create and show ContactInfoFragment
        ContactInfoFragment contactInfoFragment = ContactInfoFragment.newInstance(currentMessage);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, contactInfoFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.chat_options_menu, popup.getMenu());

        // Set text size and padding for menu items
        for (int i = 0; i < popup.getMenu().size(); i++) {
            MenuItem item = popup.getMenu().getItem(i);
            SpannableString spanString = new SpannableString(Objects.requireNonNull(popup.getMenu().getItem(i).getTitle()).toString());
            spanString.setSpan(new AbsoluteSizeSpan(16, true), 0, spanString.length(), 0);
            item.setTitle(spanString);
        }

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_clear_chat) {
                // Handle clear chat
                return true;
            } else if (itemId == R.id.menu_delete_chat) {
                // Handle delete chat
                return true;
            } else if (itemId == R.id.menu_contact_info) {
                openContactInfo();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private List<Chat> getDummyChats() {
        List<Chat> chats = new ArrayList<>();

        // Add date header for 31 Jan
        chats.add(new Chat(
                "",
                "",
                "31 Jan, 2024",
                false,
                false,
                true,
                0
        ));

        // First received message
        chats.add(new Chat(
                "Let me know when you reach at block 225, street 2, dummy road, pickering",
                "9:29 am",
                "",
                false,
                false,
                false,
                0
        ));

        // First sent message
        chats.add(new Chat(
                "Sure i will be there in next 4 hours, i will call you when i reached mentioned location by you.",
                "9:30 am",
                "",
                true,
                true,
                false,
                R.drawable.ic_delivered_blue
        ));

        // Add Today header
        chats.add(new Chat(
                "",
                "",
                "Today",
                false,
                false,
                true,
                0
        ));

        // Today's received message
        chats.add(new Chat(
                "Let me know when you reach at block 225, street 2, dummy road, pickering",
                "9:29 am",
                "",
                false,
                false,
                false,
                0
        ));

        // Today's first sent message
        chats.add(new Chat(
                "Sure i will be there in next 4 hours, i will call you when i reached mentioned location by you.",
                "9:30 am",
                "",
                true,
                true,
                false,
                R.drawable.ic_delivered_black
        ));

        // Today's second sent message
        chats.add(new Chat(
                "Sure i will be there in next 4 hours, i will call you when i reached mentioned location by you.",
                "9:30 am",
                "",
                true,
                true,
                false,
                R.drawable.ic_delivered_tick
        ));

        return chats;
    }
} 