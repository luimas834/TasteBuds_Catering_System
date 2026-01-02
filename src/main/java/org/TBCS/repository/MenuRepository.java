package org. TBCS.repository;

import org.TBCS.model.MenuItem;
import org.TBCS.util.XMLStorageUtil;
import org.w3c. dom.*;

import java.math.BigDecimal;
import java.util.*;

public class MenuRepository {
    private static final String FILENAME = "menu.xml";
    private final Map<String, MenuItem> menuItems = new LinkedHashMap<>();
    private int nextItemNumber = 1;

    public MenuRepository() {
        loadFromXML();
        if (menuItems.isEmpty()) {
            initializeDefaultMenu();
        }
    }

    private void initializeDefaultMenu() {
        save(new MenuItem(generateItemId(), "Chicken Biryani", "Main Course", new BigDecimal("350")));
        save(new MenuItem(generateItemId(), "Mutton Biryani", "Main Course", new BigDecimal("450")));
        save(new MenuItem(generateItemId(), "Vegetable Biryani", "Main Course", new BigDecimal("250")));
        save(new MenuItem(generateItemId(), "Butter Chicken", "Main Course", new BigDecimal("380")));
        save(new MenuItem(generateItemId(), "Naan", "Bread", new BigDecimal("30")));
        save(new MenuItem(generateItemId(), "Paratha", "Bread", new BigDecimal("25")));
        save(new MenuItem(generateItemId(), "Mango Lassi", "Beverage", new BigDecimal("80")));
        save(new MenuItem(generateItemId(), "Soft Drink", "Beverage", new BigDecimal("40")));
        save(new MenuItem(generateItemId(), "Gulab Jamun", "Dessert", new BigDecimal("60")));
        save(new MenuItem(generateItemId(), "Kheer", "Dessert", new BigDecimal("70")));
    }

    public String generateItemId() {
        return "M" + String.format("%03d", nextItemNumber++);
    }

    private void loadFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("menuItem");
            for (int i = 0; i < nodes. getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                String itemId = XMLStorageUtil.getElementText(elem, "itemId");
                String name = XMLStorageUtil.getElementText(elem, "name");
                String category = XMLStorageUtil.getElementText(elem, "category");
                BigDecimal price = new BigDecimal(XMLStorageUtil. getElementText(elem, "price"));
                boolean available = Boolean.parseBoolean(XMLStorageUtil.getElementText(elem, "available"));

                menuItems.put(itemId, new MenuItem(itemId, name, category, price, available));

                try {
                    int num = Integer.parseInt(itemId.substring(1));
                    if (num >= nextItemNumber) {
                        nextItemNumber = num + 1;
                    }
                } catch (NumberFormatException ignored) {}
            }
        } catch (Exception e) {
            System.err.println("Error loading menu:  " + e.getMessage());
        }
    }

    private void saveToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("menu");
            doc.appendChild(root);

            for (MenuItem item : menuItems.values()) {
                Element elem = doc.createElement("menuItem");
                XMLStorageUtil.appendElement(doc, elem, "itemId", item.getItemId());
                XMLStorageUtil.appendElement(doc, elem, "name", item.getName());
                XMLStorageUtil.appendElement(doc, elem, "category", item.getCategory());
                XMLStorageUtil.appendElement(doc, elem, "price", item.getPrice().toString());
                XMLStorageUtil.appendElement(doc, elem, "available", String.valueOf(item.isAvailable()));
                root.appendChild(elem);
            }

            XMLStorageUtil.saveDocument(doc, FILENAME);
        } catch (Exception e) {
            System. err.println("Error saving menu: " + e.getMessage());
        }
    }

    public void save(MenuItem item) {
        menuItems.put(item.getItemId(), item);
        saveToXML();
    }

    public MenuItem findById(String id) {
        return menuItems. get(id);
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems.values());
    }

    public List<MenuItem> findAvailable() {
        List<MenuItem> available = new ArrayList<>();
        for (MenuItem item : menuItems.values()) {
            if (item.isAvailable()) {
                available. add(item);
            }
        }
        return available;
    }

    public List<MenuItem> findByCategory(String category) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : menuItems. values()) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                result.add(item);
            }
        }
        return result;
    }

    public void delete(String itemId) {
        menuItems. remove(itemId);
        saveToXML();
    }

    public List<String> getCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (MenuItem item : menuItems.values()) {
            categories.add(item. getCategory());
        }
        return new ArrayList<>(categories);
    }
}