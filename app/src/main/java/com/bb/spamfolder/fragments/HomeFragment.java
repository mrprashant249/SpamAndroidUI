package com.bb.spamfolder.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.adapters.DeliveryAdapter;
import com.bb.spamfolder.models.Order;
import com.bb.spamfolder.utils.DividerItemDecoration;
import com.bb.spamfolder.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
    private View rootView;
    private Order order;

    public static HomeFragment newInstance(Order myOrder) {
        HomeFragment fragment = new HomeFragment();
        if (myOrder != null) {
            fragment.order = myOrder;
            Bundle args = new Bundle();
            args.putString("orderId", myOrder.getOrderId());
            args.putString("status", myOrder.getStatus());
            args.putString("address", myOrder.getAddress());
            args.putString("price", myOrder.getPrice());
            args.putString("eta", myOrder.getEta());
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View mapContainer = rootView.findViewById(R.id.mapContainer);
        mapContainer.setVisibility(View.VISIBLE);

        // Get the SupportMapFragment and request notification when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext()));
        // Navigate to OrderDetailFragment
        DeliveryAdapter adapter = new DeliveryAdapter(Utils.getDeliveryData(requireContext()), order -> {
            // Navigate to OrderDetailFragment
            if (order.getStatus().equalsIgnoreCase("in-progress")) {
                order.setStatus("Out for Delivery");
            }
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(order);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        ImageView calendarIcon = rootView.findViewById(R.id.calendarIcon);
        calendarIcon.setOnClickListener(v -> showDatePicker());

        showNewOrderDialog();

        // Inside onViewCreated, add click listener for filter button
        view.findViewById(R.id.btnFilter).setOnClickListener(v -> showFilterDialog());
        view.findViewById(R.id.btnCancelDelivered).setOnClickListener(v -> showCancelDeliveryDialog(view));
        view.findViewById(R.id.btnMarkAsDelivery).setOnClickListener(v -> showMarkedDialog());
        try {
            if (order != null) {
                if (order.getStatus().equalsIgnoreCase(getString(R.string.cancelled))) {
                    hideActionButton(view);
                }
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void showMarkedDialog() {
        // Create dummy order for details
        Order order = new Order(
                "01542154",
                "$6529.92",
                "Out for Delivery",
                "ETA: 2 h 43 m",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        );

        // Navigate to order details
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flContainer, OrderDetailFragment.newInstance(order))
                .addToBackStack(null)
                .commit();
    }

    private void showCancelDeliveryDialog(View vw) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_delivery, null);
        dialog.setContentView(view);

        // Remove default white background of bottom sheet
        View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(android.R.color.transparent);
        }

        // Find views
        MaterialButton btnCancelDelivery = view.findViewById(R.id.btnCancelDelivery);

        btnCancelDelivery.setOnClickListener(v -> {
            dialog.dismiss();
            hideActionButton(vw);
        });

        dialog.show();
    }

    private void hideActionButton(View view) {
        try {
            ((TextView) view.findViewById(R.id.tvStatusName)).setText(R.string.cancelled);
            view.findViewById(R.id.ivCircularCancelled).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ivCircularSuccess).setVisibility(View.GONE);
            view.findViewById(R.id.actionLayout).setVisibility(View.GONE);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    private void setupViews() {
        final SwitchCompat onlineSwitch = rootView.findViewById(R.id.onlineSwitch);
        final TextView tvOnlineOffline = rootView.findViewById(R.id.tvOnlineOffline);

        onlineSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                tvOnlineOffline.setText(R.string.you_re_online);
            } else {
                tvOnlineOffline.setText(R.string.you_re_offline);
            }
        });

        // Set up blocked message with underlined delivery number
        TextView tvBlockedMessage = rootView.findViewById(R.id.tvBlockedMessage);
        String fullText = getString(R.string.you_are_blocked);
        SpannableString spannableString = new SpannableString(fullText);

        // Find the position of the delivery number
        int startPos = fullText.indexOf("#");
        int endPos = startPos + 10; // Length of "#123456778"

        // Add underline span
        spannableString.setSpan(new UnderlineSpan(), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBlockedMessage.setText(spannableString);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.setInfoWindowAdapter(this);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        // Add marker
        LatLng location = new LatLng(43.8384, -79.0868); // Pickering coordinates
        MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .title("Tracy Williams")
                .snippet("2720 Pure Springs Blvd., Pickering, ON L1X 0C4|In-progress");

        Marker marker = googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

        // Show info window immediately
        if (marker != null) {
            marker.showInfoWindow();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.map_marker_content, null);

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvStatus = view.findViewById(R.id.tvStatus);

        String[] snippetParts = Objects.requireNonNull(marker.getSnippet()).split("\\|");
        String status = snippetParts[1];

        tvName.setText(marker.getTitle());
        tvAddress.setText(snippetParts[0]);
        tvStatus.setText(status);

        // Set status background and text color based on status
        if (status.equalsIgnoreCase("In-progress")) {
            tvStatus.setBackgroundResource(R.drawable.bg_status_out_delivery);
            tvStatus.setTextColor(requireContext().getColor(R.color.orange));
        } else if (status.equalsIgnoreCase("Delivered")) {
            tvStatus.setBackgroundResource(R.drawable.bg_status_delivered);
            tvStatus.setTextColor(requireContext().getColor(R.color.green));
        } else if (status.equalsIgnoreCase("Cancelled")) {
            tvStatus.setBackgroundResource(R.drawable.bg_status_cancelled);
            tvStatus.setTextColor(requireContext().getColor(R.color.red_button));
        } else {
            tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
            tvStatus.setTextColor(requireContext().getColor(R.color.blue_button));
        }

        return view;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    private void showDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTheme(R.style.ThemeMaterialCalendar)
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            String formattedDate = sdf.format(new Date(selection));
            Toast.makeText(requireContext(), "Selected: " + formattedDate, Toast.LENGTH_SHORT).show();
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private void showFilterDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_filter, null);
        dialog.setContentView(view);

        // Remove default white background of bottom sheet
        View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(android.R.color.transparent);
        }

        RadioGroup filterGroup = view.findViewById(R.id.filterGroup);
        filterGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String filter = "";
            if (checkedId == R.id.rbPending) {
                filter = "Pending";
            } else if (checkedId == R.id.rbInProgress) {
                filter = "In-Progress";
            } else if (checkedId == R.id.rbDelivered) {
                filter = "Delivered";
            } else if (checkedId == R.id.rbCanceled) {
                filter = "Cancelled";
            }
            Utils.sout("Filter item: " + filter);

            // TODO: Apply filter to delivery list
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showNewOrderDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_new_order);

        // Set dialog width to match parent with margins
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.CENTER);
        }

        // Handle button clicks
        dialog.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
            dialog.dismiss();
            // Create dummy order for details
            Order order = new Order(
                    "231541547",
                    "$6529.92",
                    "Pending",
                    "ETA: 2 h 43 m",
                    "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
            );

            // Navigate to order details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, OrderDetailFragment.newInstance(order))
                    .addToBackStack(null)
                    .commit();
        });

        dialog.show();
    }
} 