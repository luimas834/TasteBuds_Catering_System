package org. TBCS.repository;

import org.TBCS.model. Chef;
import org.TBCS.util.XMLStorageUtil;
import org.w3c.dom.*;

import java.util.*;

public class ChefRepository {
    private static final String FILENAME = "chefs.xml";
    private final Map<String, Chef> chefs = new HashMap<>();

    public ChefRepository() {
        loadFromXML();
        if (chefs.isEmpty()) {
            initializeDefaultChefs();
        }
    }

    private void initializeDefaultChefs() {
        save(new Chef("Karim", "Bengali Cuisine"));
        save(new Chef("Hassan", "Mughlai"));
        save(new Chef("Rahim", "Continental"));
    }

    private void loadFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("chef");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                String chefId = XMLStorageUtil.getElementText(elem, "chefId");
                String name = XMLStorageUtil.getElementText(elem, "name");
                String specialization = XMLStorageUtil.getElementText(elem, "specialization");
                boolean available = Boolean.parseBoolean(XMLStorageUtil.getElementText(elem, "available"));

                chefs.put(chefId, new Chef(chefId, name, specialization, available));
            }
        } catch (Exception e) {
            System.err.println("Error loading chefs: " + e.getMessage());
        }
    }

    private void saveToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("chefs");
            doc.appendChild(root);

            for (Chef chef : chefs.values()) {
                Element elem = doc.createElement("chef");
                XMLStorageUtil.appendElement(doc, elem, "chefId", chef. getChefId());
                XMLStorageUtil.appendElement(doc, elem, "name", chef. getName());
                XMLStorageUtil.appendElement(doc, elem, "specialization", chef.getSpecialization());
                XMLStorageUtil.appendElement(doc, elem, "available", String.valueOf(chef.isAvailable()));
                root.appendChild(elem);
            }

            XMLStorageUtil. saveDocument(doc, FILENAME);
        } catch (Exception e) {
            System.err.println("Error saving chefs: " + e.getMessage());
        }
    }

    public void save(Chef chef) {
        chefs.put(chef.getChefId(), chef);
        saveToXML();
    }

    public Chef findById(String id) {
        return chefs.get(id);
    }

    public List<Chef> findAll() {
        return new ArrayList<>(chefs.values());
    }

    public List<Chef> findAvailable() {
        List<Chef> available = new ArrayList<>();
        for (Chef chef : chefs. values()) {
            if (chef.isAvailable()) {
                available.add(chef);
            }
        }
        return available;
    }

    public void delete(String chefId) {
        chefs.remove(chefId);
        saveToXML();
    }
}