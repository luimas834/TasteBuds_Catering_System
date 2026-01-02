package org.TBCS.model;

import java. util.UUID;

public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;
    private boolean registered;
    private int monthlyOrderCount;
    private int totalOrderCount;

    public Customer(String name, String phone, String email, boolean registered) {
        this.customerId = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.registered = registered;
        this.monthlyOrderCount = 0;
        this.totalOrderCount = 0;
    }

    public Customer(String customerId, String name, String phone, String email,
                    boolean registered, int monthlyOrderCount, int totalOrderCount) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.registered = registered;
        this.monthlyOrderCount = monthlyOrderCount;
        this.totalOrderCount = totalOrderCount;
    }

    public void incrementOrderCount() {
        this.monthlyOrderCount++;
        this. totalOrderCount++;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public boolean isRegistered() { return registered; }
    public int getMonthlyOrderCount() { return monthlyOrderCount; }
    public int getTotalOrderCount() { return totalOrderCount; }
    public void setRegistered(boolean registered) { this.registered = registered; }

    @Override
    public String toString() {
        return name + " (" + phone + ")" + (registered ? " [Registered]" : "");
    }
}