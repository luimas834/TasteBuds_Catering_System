package org.TBCS.repository;

import org.TBCS.model.Vehicle;
import org. TBCS.util.XMLStorageUtil;
import org.w3c.dom.*;

import java. util.*;

public class VehicleRepository {
    private static final String FILENAME = "vehicles.xml";
    private final Map<String, Vehicle> vehicles = new HashMap<>();

    public VehicleRepository() {
        loadFromXML();
        if (vehicles.isEmpty()) {
            initializeDefaultVehicles();
        }
    }

    private void initializeDefaultVehicles() {
        save(new Vehicle("DHK-1234", "Motorcycle"));
        save(new Vehicle("DHK-5678", "Car"));
        save(new Vehicle("DHK-9012", "Motorcycle"));
    }

    private void loadFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("vehicle");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                String vehicleId = XMLStorageUtil.getElementText(elem, "vehicleId");
                String regNumber = XMLStorageUtil.getElementText(elem, "registrationNumber");
                String type = XMLStorageUtil.getElementText(elem, "vehicleType");
                boolean available = Boolean.parseBoolean(XMLStorageUtil.getElementText(elem, "available"));

                vehicles.put(vehicleId, new Vehicle(vehicleId, regNumber, type, available));
            }
        } catch (Exception e) {
            System.err. println("Error loading vehicles: " + e.getMessage());
        }
    }

    private void saveToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("vehicles");
            doc.appendChild(root);

            for (Vehicle vehicle : vehicles.values()) {
                Element elem = doc.createElement("vehicle");
                XMLStorageUtil.appendElement(doc, elem, "vehicleId", vehicle.getVehicleId());
                XMLStorageUtil.appendElement(doc, elem, "registrationNumber", vehicle.getRegistrationNumber());
                XMLStorageUtil.appendElement(doc, elem, "vehicleType", vehicle. getVehicleType());
                XMLStorageUtil.appendElement(doc, elem, "available", String.valueOf(vehicle.isAvailable()));
                root.appendChild(elem);
            }

            XMLStorageUtil.saveDocument(doc, FILENAME);
        } catch (Exception e) {
            System. err.println("Error saving vehicles: " + e.getMessage());
        }
    }

    public void save(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleId(), vehicle);
        saveToXML();
    }

    public Vehicle findById(String id) {
        return vehicles. get(id);
    }

    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles. values());
    }

    public List<Vehicle> findAvailable() {
        List<Vehicle> available = new ArrayList<>();
        for (Vehicle vehicle : vehicles.values()) {
            if (vehicle.isAvailable()) {
                available.add(vehicle);
            }
        }
        return available;
    }

    public Vehicle findFirstAvailable() {
        for (Vehicle vehicle : vehicles.values()) {
            if (vehicle.isAvailable()) {
                return vehicle;
            }
        }
        return null;
    }

    public void delete(String vehicleId) {
        vehicles.remove(vehicleId);
        saveToXML();
    }
}