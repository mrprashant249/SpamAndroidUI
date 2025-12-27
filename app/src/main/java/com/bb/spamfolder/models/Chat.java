package com.bb.spamfolder.models;

public record Chat(String message, String time, String date, boolean isSent, boolean isDelivered,
                   boolean showDate, int deliverImage) {
}