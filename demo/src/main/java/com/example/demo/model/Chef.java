package com.example.demo.model;

public class Chef {
    private int timeSpent;
    private int orderTime;
    private int orderId;

    private boolean busy;

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Chef() {
        clear();
    }

    public void clear() {

        this.timeSpent = 0;
        this.orderId = 0;
        this.orderTime = 0;
        this.busy = false;

    }

    public void assignOrder(int orderId, int orderTime, int timeSpent) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.timeSpent = timeSpent;
        this.busy = true;
    }

    public void increment() {
        this.timeSpent += 1;
    }

    public boolean finishedOrder() {
        return this.timeSpent == this.orderTime;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(int timeSpent) {
        this.timeSpent = timeSpent;
    }

    public int getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(int orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
