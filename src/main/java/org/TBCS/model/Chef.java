package org.TBCS.model;

import java. util.UUID;

public class Chef {
    private String chefId;
    private String name;
    private String specialization;
    private boolean available;

    public Chef(String name, String specialization) {
        this.chefId = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.specialization = specialization;
        this.available = true;
    }

    public Chef(String chefId, String name, String specialization, boolean available) {
        this.chefId = chefId;
        this. name = name;
        this. specialization = specialization;
        this.available = available;
    }

    public String getChefId() { return chefId; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setName(String name) { this.name = name; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return String. format("[%s] %s (%s)%s", chefId, name, specialization, available ? "" : " [Busy]");
    }
}