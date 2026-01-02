package org.TBCS.model;

import java.time.LocalDate;

public class DrivingLicense {
    private String licenseNumber;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String licenseType;

    public DrivingLicense(String licenseNumber, LocalDate issueDate,
                          LocalDate expiryDate, String licenseType) {
        this.licenseNumber = licenseNumber;
        this. issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.licenseType = licenseType;
    }

    public boolean isValid() {
        return LocalDate.now().isBefore(expiryDate);
    }

    public String getLicenseNumber() { return licenseNumber; }
    public LocalDate getIssueDate() { return issueDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getLicenseType() { return licenseType; }

    @Override
    public String toString() {
        return String.format("License:  %s (Type: %s, Expires: %s)%s",
                licenseNumber, licenseType, expiryDate, isValid() ? "" : " [EXPIRED]");
    }
}