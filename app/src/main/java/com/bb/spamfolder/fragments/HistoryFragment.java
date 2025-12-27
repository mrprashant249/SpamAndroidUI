package com.bb.spamfolder.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.adapters.OrderAdapter;
import com.bb.spamfolder.models.Order;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    private BarChart deliveriesChart, collectionChart;
    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        setupCharts(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOrdersList(view);
        setupSearch(view);

    }

    private void setupCharts(View view) {
        deliveriesChart = view.findViewById(R.id.deliveriesChart);
        collectionChart = view.findViewById(R.id.collectionChart);

        EditText etFrom = view.findViewById(R.id.etFrom);
        EditText etTo = view.findViewById(R.id.etTo);
        etFrom.setFocusable(false);
        etFrom.clearFocus();
        etTo.setFocusable(false);
        etTo.clearFocus();
        etFrom.setOnClickListener(v -> showDatePicker());
        etTo.setOnClickListener(v -> showDatePicker());

        setupDeliveriesChart();
        setupCollectionChart();
    }

    private void setupDeliveriesChart() {
        // Data
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 2999)); // Wed
        entries.add(new BarEntry(1, 3999)); // Thu
        entries.add(new BarEntry(2, 3499)); // Fri
        entries.add(new BarEntry(3, 4199)); // Sat
        entries.add(new BarEntry(4, 2499)); // Sun
        entries.add(new BarEntry(5, 1799)); // Mon
        entries.add(new BarEntry(6, 2999)); // Tue

        BarDataSet dataSet = new BarDataSet(entries, "Deliveries");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.blue_button));
        dataSet.setDrawValues(false);

        // X-axis labels
        final String[] labels = new String[]{"Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue"};
        XAxis xAxis = deliveriesChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // Y-axis setup
        YAxis leftAxis = deliveriesChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);

        // Disable right Y-axis
        deliveriesChart.getAxisRight().setEnabled(false);

        // Other chart settings
        deliveriesChart.setData(new BarData(dataSet));
        deliveriesChart.getDescription().setEnabled(false);
        deliveriesChart.getLegend().setEnabled(false);
        deliveriesChart.setDrawGridBackground(false);
        deliveriesChart.setTouchEnabled(false);
        deliveriesChart.invalidate();
    }

    private void setupCollectionChart() {
        // Data
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 2999)); // Wed
        entries.add(new BarEntry(1, 3999)); // Thu
        entries.add(new BarEntry(2, 3499)); // Fri
        entries.add(new BarEntry(3, 4199)); // Sat
        entries.add(new BarEntry(4, 2499)); // Sun
        entries.add(new BarEntry(5, 1799)); // Mon
        entries.add(new BarEntry(6, 2999)); // Tue

        BarDataSet dataSet = new BarDataSet(entries, "Collections");
        dataSet.setColor(ContextCompat.getColor(requireContext(), R.color.blue_button));
        dataSet.setDrawValues(false);

        // X-axis labels
        final String[] labels = new String[]{"Wed", "Thu", "Fri", "Sat", "Sun", "Mon", "Tue"};
        XAxis xAxis = collectionChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // Y-axis setup
        YAxis leftAxis = collectionChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);

        // Disable right Y-axis
        collectionChart.getAxisRight().setEnabled(false);

        // Other chart settings
        collectionChart.setData(new BarData(dataSet));
        collectionChart.getDescription().setEnabled(false);
        collectionChart.getLegend().setEnabled(false);
        collectionChart.setDrawGridBackground(false);
        collectionChart.setTouchEnabled(false);
        collectionChart.invalidate();
    }

    private void setupOrdersList(View view) {
        rvOrders = view.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<Order> ordersList = getDummyOrders();
        orderAdapter = new OrderAdapter(ordersList, order -> {
            // Navigate to OrderDetailFragment
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(order);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvOrders.setAdapter(orderAdapter);
    }

    private void setupSearch(View view) {
        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterOrders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterOrders(String query) {
        List<Order> filteredList = new ArrayList<>();
        for (Order order : getDummyOrders()) {
            if (order.getOrderId().toLowerCase().contains(query.toLowerCase()) ||
                    order.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(order);
            }
        }
        orderAdapter = new OrderAdapter(filteredList, order -> {
            OrderDetailFragment fragment = OrderDetailFragment.newInstance(order);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvOrders.setAdapter(orderAdapter);
    }

    private List<Order> getDummyOrders() {
        List<Order> orders = new ArrayList<>();

        orders.add(new Order(
                "01542154",
                "$6529.92",
                "Pending",
                "ETA: 2 h 43 m",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "23515825",
                "$12353.32",
                "Delivered",
                "9 Jan, 2025 - 09:21 AM",
                "1725 Pure Springs Blvd., Dummy Road, Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "13456789",
                "$6529.92",
                "Out for Delivery",
                "ETA: 2 h 43 m",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "01542154",
                "$6529.92",
                "Cancelled",
                "Cancelled by Driver",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "13456789",
                "$6529.92",
                "Out for Delivery",
                "ETA: 2 h 43 m",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "23515825",
                "$12353.32",
                "Delivered",
                "9 Jan, 2025 - 09:21 AM",
                "1725 Pure Springs Blvd., Dummy Road, Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "01542154",
                "$6529.92",
                "Pending",
                "ETA: 2 h 43 m",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        orders.add(new Order(
                "01542154",
                "$6529.92",
                "Cancelled",
                "Cancelled by Driver",
                "1725 Pure Springs Blvd., Pickering, ON L1X 0C4, Canada"
        ));

        return orders;
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
} 