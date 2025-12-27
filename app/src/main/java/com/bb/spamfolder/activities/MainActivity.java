package com.bb.spamfolder.activities;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bb.spamfolder.R;
import com.bb.spamfolder.fragments.HistoryFragment;
import com.bb.spamfolder.fragments.HomeFragment;
import com.bb.spamfolder.fragments.MessageFragment;
import com.bb.spamfolder.fragments.ProfileFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    //    private BadgeDrawable messageBadge;
    private BadgeDrawable historyDot, messageDot, profileDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        setupBottomNavigation();
        setupBadges();

        // Show home fragment by default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContainer, new HomeFragment())
                .commit();

        goBack();
    }

    private void showAll(BadgeDrawable... badges) {
        for (BadgeDrawable badge : badges) {
            if (badge != null) {
                badge.setVisible(true);
            }
        }
    }

    private void hideOne(BadgeDrawable badge) {
        if (badge != null) {
            badge.setVisible(false);
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            showAll(historyDot, messageDot, profileDot);

            if (item.getItemId() == R.id.menu_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.menu_history) {
                selectedFragment = new HistoryFragment();
                // Remove history badge when clicked
                hideOne(historyDot);
            } else if (item.getItemId() == R.id.menu_message) {
                selectedFragment = new MessageFragment();
                // Remove message badge when clicked
                hideOne(messageDot);
//                if (messageBadge != null) {
//                    messageBadge.setVisible(false);
//                }
            } else if (item.getItemId() == R.id.menu_profile) {
                selectedFragment = new ProfileFragment();
                hideOne(profileDot);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flContainer, selectedFragment)
                        .commit();
                return true;
            }
            return false;
        });
    }

    private void setupBadges() {
        // Setup message count badge
//        messageBadge = bottomNavigationView.getOrCreateBadge(R.id.menu_message);
//        messageBadge.setBackgroundColor(ContextCompat.getColor(this, R.color.red_button));
//        messageBadge.setBadgeTextColor(ContextCompat.getColor(this, R.color.white));
//        messageBadge.setNumber(5); // Set your message count here
//        messageBadge.setVisible(true);

        // Setup dot badge
        historyDot = bottomNavigationView.getOrCreateBadge(R.id.menu_history);
        messageDot = bottomNavigationView.getOrCreateBadge(R.id.menu_message);
        profileDot = bottomNavigationView.getOrCreateBadge(R.id.menu_profile);
        initBadgeDot(historyDot, messageDot, profileDot);
    }

    private void initBadgeDot(BadgeDrawable... badges) {
        for (BadgeDrawable badge : badges) {
            badge.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_button));
            badge.setVisible(true);
            badge.setMaxCharacterCount(0);
        }
    }

    // Method to update badge count (if needed)
//    public void updateMessageBadge(int count) {
//        if (messageBadge != null) {
//            if (count > 0) {
//                messageBadge.setNumber(count);
//                messageBadge.setVisible(true);
//            } else {
//                messageBadge.setVisible(false);
//            }
//        }
//    }

    // Method to show/hide history dot (if needed)
//    public void setHistoryDotVisible(boolean visible) {
//        if (historyDot != null) {
//            historyDot.setVisible(visible);
//        }
//    }

//// To update message badge count
//((MainActivity) requireActivity()).updateMessageBadge(newCount);

    //// To show/hide history dot
//((MainActivity) requireActivity()).setHistoryDotVisible(false);
    private void goBack() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finishAffinity();
                }
            }
        });
    }
} 