package com.bb.spamfolder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bb.spamfolder.R;
import com.bb.spamfolder.models.Order;

import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {
    private final List<Order> deliveries;
    private static OrderAdapter.OnOrderClickListener listener;

    public DeliveryAdapter(List<Order> deliveries, OrderAdapter.OnOrderClickListener listener) {
        this.deliveries = deliveries;
        DeliveryAdapter.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order delivery = deliveries.get(position);
        holder.bind(delivery);
    }

    @Override
    public int getItemCount() {
        return deliveries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAddress;
        private final TextView tvPrice;
        private final TextView tvOrderId;
        private final TextView tvStatus;
        private final TextView tvEta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvEta = itemView.findViewById(R.id.tvEta);
        }

        public void bind(Order delivery) {
            tvAddress.setText(delivery.getAddress());
            tvPrice.setText(delivery.getPrice());
            tvOrderId.setText(delivery.getOrderId());
            tvStatus.setText(delivery.getStatus());

            switch (delivery.getStatus()) {
                case "Delivered":
                    tvEta.setText(delivery.getDeliveredTime());
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.green_switch));
                    break;
                case "Cancelled":
                    tvEta.setText(itemView.getContext().getString(R.string.cancelled_by_driver));
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.red_button));
                    break;
                case "Pending":
                    tvEta.setText(delivery.getEta());
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.blue_button));
                    break;
                default:
                    tvEta.setText(delivery.getEta());
                    tvStatus.setTextColor(itemView.getContext().getColor(R.color.orange));
                    break;
            }

            // Add click listener to the entire item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(delivery);
                }
            });
        }

    }
} 