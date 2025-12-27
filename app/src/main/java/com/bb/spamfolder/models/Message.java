package com.bb.spamfolder.models;

public record Message(String name, String message, String time, int messageCount, boolean isMale,
                      boolean isDelivered, int drawableImage, int deliverImage) {
}