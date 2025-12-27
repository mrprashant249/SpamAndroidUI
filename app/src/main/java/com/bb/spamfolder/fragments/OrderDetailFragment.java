package com.bb.spamfolder.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.adapters.DeliveryAdapter;
import com.bb.spamfolder.models.Message;
import com.bb.spamfolder.models.Order;
import com.bb.spamfolder.utils.DividerItemDecoration;
import com.bb.spamfolder.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class OrderDetailFragment extends Fragment implements OnMapReadyCallback {
    private TextView tvOrderId;
    public Order order;

    public static OrderDetailFragment newInstance(Order myOrder) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        fragment.order = myOrder;
        Bundle args = new Bundle();
        args.putString("orderId", myOrder.getOrderId());
        args.putString("status", myOrder.getStatus());
        args.putString("address", myOrder.getAddress());
        args.putString("price", myOrder.getPrice());
        args.putString("eta", myOrder.getEta());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivBack = view.findViewById(R.id.ivBack);
        tvOrderId = view.findViewById(R.id.tvOrderId);
        TextView tvStatus = view.findViewById(R.id.tvStatus);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView btnChatAdmin = view.findViewById(R.id.btnChatAdmin);
        TextView txtDeliveryInProgress = view.findViewById(R.id.txtDeliveryInProgress);

        (view.findViewById(R.id.mapView)).setVisibility(View.VISIBLE);

        // Set data from arguments
        if (getArguments() != null) {
            String orderId = getArguments().getString("orderId", "");
            String status = getArguments().getString("status", "");
            String address = getArguments().getString("address", "");
//            String eta = getArguments().getString("eta", "");

            if (!orderId.startsWith("#"))
                orderId = "#" + orderId;
            tvOrderId.setText(orderId);
            tvStatus.setText(status);
            tvAddress.setText(address);

            // Set status background and color
            int backgroundRes;
            int textColor;
            int deliveryStatus = R.string.delivery_started;
            switch (status.toLowerCase()) {
                case "pending":
                    backgroundRes = R.drawable.bg_status_pending;
                    textColor = requireContext().getColor(R.color.blue_button);
                    deliveryStatus = R.string.delivery_in_progress;
                    break;
                case "delivered":
                    backgroundRes = R.drawable.bg_status_delivered;
                    textColor = requireContext().getColor(R.color.green);
                    break;
                case "out for delivery":
                    backgroundRes = R.drawable.bg_status_out_delivery;
                    textColor = requireContext().getColor(R.color.orange);
                    break;
                case "cancelled":
                    backgroundRes = R.drawable.bg_status_cancelled;
                    textColor = requireContext().getColor(R.color.red_button);
                    break;
                default:
                    backgroundRes = R.drawable.bg_status_pending;
                    textColor = requireContext().getColor(R.color.text_secondary);
            }
            tvStatus.setBackgroundResource(backgroundRes);
            tvStatus.setTextColor(textColor);
            txtDeliveryInProgress.setText(deliveryStatus);
        }

        // Handle back button click
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Handle chat with admin click
        btnChatAdmin.setOnClickListener(v -> {
            // Navigate to ChatDetailFragment
            ChatDetailFragment fragment = ChatDetailFragment.newInstance(
                    new Message(
                            "Dispatcher 123",
                            "I Just Delivered #12456541427 to the buyer...",
                            "Yesterday",
                            0,
                            false,
                            true,
                            R.drawable.profile_male_2,
                            R.drawable.ic_delivered_black
                    )
            );

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Get the SupportMapFragment and request notification when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_details);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupTimeline(view);

        // Inside onViewCreated, find and set click listener for Cancel Delivery button
        view.findViewById(R.id.btnStartDelivery).setOnClickListener(v -> {
                    order.setStatus("Out for Delivery");
                    OrderDetailFragment fragment = OrderDetailFragment.newInstance(order);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContainer, fragment)
                            .addToBackStack(null)
                            .commit();
                }
        );
        view.findViewById(R.id.btnCancelDel).setOnClickListener(v -> showCancelDeliveryDialog());
        view.findViewById(R.id.btnCancelDelivery).setOnClickListener(v -> showCancelDeliveryDialog());
        view.findViewById(R.id.btnMarkAsDelivery).setOnClickListener(v -> showMarkedDialog());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Delivery location (Point B) - Pickering
        LatLng deliveryLocation = new LatLng(43.8384, -79.0868);
        LatLng currentLocation = new LatLng(43.8084, -79.0868);// Current location (Point A) - About 5km away

//        LatLng deliveryLocation = new LatLng(28.5021359, 77.4054901);
//        LatLng currentLocation = new LatLng(28.5151087, 77.3932163);

        // Add marker for current location (Point A)
        googleMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        // Add marker for delivery location (Point B)
        googleMap.addMarker(new MarkerOptions()
                .position(deliveryLocation)
                .title("Delivery Location")
                .snippet("Tracy Williams\n2720 Pure Springs Blvd., Pickering"));

        // Include both points in the camera view with some padding
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLocation);
        builder.include(deliveryLocation);
        LatLngBounds bounds = builder.build();

        // Move camera to show both points with padding
        int padding = 100;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));

        // Disable map toolbar and zoom controls for cleaner look
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }


    private void setupTimeline(View view) {

        // Find all timeline items
        LinearLayout deliveryInProgress = view.findViewById(R.id.deliveryInProgress);
        View line1 = view.findViewById(R.id.line1);
        TextView eta5Min = view.findViewById(R.id.eta5Min);
        View line2 = view.findViewById(R.id.line2);
        TextView eta3Min = view.findViewById(R.id.eta3Min);
        View line3 = view.findViewById(R.id.line3);
        LinearLayout delivered = view.findViewById(R.id.delivered);
        View line4 = view.findViewById(R.id.line4);
        LinearLayout cancelled = view.findViewById(R.id.cancelled);
        View line5 = view.findViewById(R.id.line5);
        LinearLayout destinations = view.findViewById(R.id.destinations);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext()));
        // Navigate to OrderDetailFragment
        DeliveryAdapter adapter = new DeliveryAdapter(Utils.getOrderDetailsData(requireContext()), order -> {
            // Navigate to OrderDetailFragment
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(order);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        View deliveryLayout = view.findViewById(R.id.deliveryLayout);//Grid has 4 items in 2 row each
        View productQtyButtonLayout = view.findViewById(R.id.productQtyButtonLayout);// List of 2 Product F with two buttons
        View productQtyLayout = view.findViewById(R.id.productQtyLayout);// List of 4 Product F with amount and qty
        View selectPackageLayout = view.findViewById(R.id.selectPackageLayout);// 4 circle for select package.
        View actionDeliveryButton = view.findViewById(R.id.actionDeliveryButton); //two button
        View onlyProductLayout = view.findViewById(R.id.onlyProductLayout);

        String status = requireArguments().getString("status", "").toLowerCase();

        line5.setVisibility(View.GONE);
        destinations.setVisibility(View.GONE);

        selectPackageLayout.setVisibility(View.GONE);
        actionDeliveryButton.setVisibility(View.GONE);

        onlyProductLayout.setVisibility(View.GONE);
        // Show/hide timeline items based on status
        switch (status) {
            case "pending":
                deliveryInProgress.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                eta5Min.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                eta3Min.setVisibility(View.VISIBLE);
                line3.setVisibility(View.VISIBLE);
                delivered.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                cancelled.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.GONE);
                productQtyButtonLayout.setVisibility(View.VISIBLE);
                productQtyLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case "out for delivery":
                deliveryInProgress.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                eta5Min.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                eta3Min.setVisibility(View.VISIBLE);
                line3.setVisibility(View.GONE);
                delivered.setVisibility(View.GONE);
                line4.setVisibility(View.GONE);
                cancelled.setVisibility(View.GONE);
                line5.setVisibility(View.VISIBLE);
                destinations.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.GONE);
                productQtyLayout.setVisibility(View.GONE);
                selectPackageLayout.setVisibility(View.VISIBLE);
                actionDeliveryButton.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case "delivered":
                deliveryInProgress.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                eta5Min.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                eta3Min.setVisibility(View.VISIBLE);
                line3.setVisibility(View.VISIBLE);
                delivered.setVisibility(View.VISIBLE);
                line4.setVisibility(View.GONE);
                cancelled.setVisibility(View.GONE);
                deliveryLayout.setVisibility(View.VISIBLE);
                productQtyButtonLayout.setVisibility(View.GONE);
                productQtyLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                break;
            case "cancelled":
                deliveryInProgress.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                eta5Min.setVisibility(View.VISIBLE);
                line2.setVisibility(View.VISIBLE);
                eta3Min.setVisibility(View.VISIBLE);
                line3.setVisibility(View.GONE);
                delivered.setVisibility(View.GONE);
                line4.setVisibility(View.VISIBLE);
                cancelled.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.GONE);
                productQtyLayout.setVisibility(View.GONE);
                onlyProductLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showCancelDeliveryDialog() {
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
            order.setStatus(getString(R.string.cancelled));
//            MainActivity.homeMenuSelected();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flContainer, HomeFragment.newInstance(order))
                    .commit();
        });

        dialog.show();
    }

    private void showMarkedDialog() {
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

        ((TextView) dialog.findViewById(R.id.tvDriverName)).setText("Good Job!");
        ((TextView) dialog.findViewById(R.id.tvOrderMessage)).setText("You Just Delivered " + tvOrderId.getText());
        // Handle button clicks
        dialog.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
            dialog.dismiss();
            // Create dummy order for details
            Order order = new Order(
                    tvOrderId.getText().toString().trim(),
                    "$6529.92",
                    "Delivered",
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