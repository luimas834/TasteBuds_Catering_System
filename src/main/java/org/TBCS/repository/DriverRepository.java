package org. TBCS.repository;

import org.TBCS.model.Driver;
import org.TBCS.model.DrivingLicense;
import org.TBCS.util.XMLStorageUtil;
import org. w3c.dom.*;

import java.time.LocalDate;
import java. util.*;

public class DriverRepository {
    private static final String FILENAME = "drivers.xml";
    private final Map<String, Driver> drivers = new HashMap<>();

    public DriverRepository() {
        loadFromXML();
        if (drivers.isEmpty()) {
            initializeDefaultDrivers();
        }
    }

    private void initializeDefaultDrivers() {
        DrivingLicense license1 = new DrivingLicense("DL-2024-001",
                LocalDate.of(2024, 1, 1), LocalDate.of(2029, 1, 1), "Commercial");
        save(new Driver("Salam", "01898765432", license1));

        DrivingLicense license2 = new DrivingLicense("DL-2024-002",
                LocalDate.of(2024, 6, 1), LocalDate.of(2029, 6, 1), "Commercial");
        save(new Driver("Rafiq", "01787654321", license2));
    }

    private void loadFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("driver");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                String driverId = XMLStorageUtil.getElementText(elem, "driverId");
                String name = XMLStorageUtil.getElementText(elem, "name");
                String phone = XMLStorageUtil.getElementText(elem, "phone");
                String licenseNumber = XMLStorageUtil. getElementText(elem, "licenseNumber");
                LocalDate issueDate = LocalDate.parse(XMLStorageUtil.getElementText(elem, "licenseIssueDate"));
                LocalDate expiryDate = LocalDate. parse(XMLStorageUtil.getElementText(elem, "licenseExpiryDate"));
                String licenseType = XMLStorageUtil. getElementText(elem, "licenseType");
                boolean available = Boolean.parseBoolean(XMLStorageUtil.getElementText(elem, "available"));

                DrivingLicense license = new DrivingLicense(licenseNumber, issueDate, expiryDate, licenseType);
                drivers.put(driverId, new Driver(driverId, name, phone, license, available));
            }
        } catch (Exception e) {
            System.err.println("Error loading drivers: " + e.getMessage());
        }
    }

    private void saveToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("drivers");
            doc.appendChild(root);

            for (Driver driver :  drivers.values()) {
                Element elem = doc.createElement("driver");
                XMLStorageUtil.appendElement(doc, elem, "driverId", driver.getDriverId());
                XMLStorageUtil. appendElement(doc, elem, "name", driver.getName());
                XMLStorageUtil.appendElement(doc, elem, "phone", driver.getPhone());
                XMLStorageUtil.appendElement(doc, elem, "licenseNumber", driver.getDrivingLicense().getLicenseNumber());
                XMLStorageUtil.appendElement(doc, elem, "licenseIssueDate", driver.getDrivingLicense().getIssueDate().toString());
                XMLStorageUtil.appendElement(doc, elem, "licenseExpiryDate", driver.getDrivingLicense().getExpiryDate().toString());
                XMLStorageUtil.appendElement(doc, elem, "licenseType", driver.getDrivingLicense().getLicenseType());
                XMLStorageUtil.appendElement(doc, elem, "available", String.valueOf(driver.isAvailable()));
                root.appendChild(elem);
            }

            XMLStorageUtil.saveDocument(doc, FILENAME);
        } catch (Exception e) {
            System.err.println("Error saving drivers: " + e.getMessage());
        }
    }

    public void save(Driver driver) {
        drivers.put(driver. getDriverId(), driver);
        saveToXML();
    }

    public Driver findById(String id) {
        return drivers. get(id);
    }

    public Driver findByLicenseNumber(String licenseNumber) {
        for (Driver driver : drivers.values()) {
            if (driver.getDrivingLicense().getLicenseNumber().equals(licenseNumber)) {
                return driver;
            }
        }
        return null;
    }

    public List<Driver> findAll() {
        return new ArrayList<>(drivers.values());
    }

    public List<Driver> findAvailable() {
        List<Driver> available = new ArrayList<>();
        for (Driver driver :  drivers.values()) {
            if (driver.isAvailable() && driver.hasValidLicense()) {
                available.add(driver);
            }
        }
        return available;
    }

    public void delete(String driverId) {
        drivers.remove(driverId);
        saveToXML();
    }
}