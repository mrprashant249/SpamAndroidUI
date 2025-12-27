package com.bb.spamfolder.models;

public class Order {
    private final String orderId;
    private final String price;
    private String status;
    private final String eta;
    private final String address;
    private String deliveredTime;

    public Order(String orderId, String price, String status, String eta, String address) {
        this.orderId = orderId;
        this.price = price;
        this.status = status;
        this.eta = eta;
        this.address = address;
    }

    public Order(String address, String price, String orderId, String status, String eta, String deliveredTime) {
        this.address = address;
        this.price = price;
        this.orderId = orderId;
        this.status = status;
        this.eta = eta;
        this.deliveredTime = deliveredTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPrice() {
        return price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getEta() {
        return eta;
    }

    public String getAddress() {
        return address;
    }

    public String getDeliveredTime() {
        return deliveredTime;
    }
}