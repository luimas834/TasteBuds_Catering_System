package org. TBCS.repository;

import org.TBCS.model.Customer;
import org.TBCS.util.XMLStorageUtil;
import org. w3c.dom.*;

import java.util.*;

public class CustomerRepository {
    private static final String FILENAME = "customers.xml";
    private final Map<String, Customer> customers = new HashMap<>();

    public CustomerRepository() {
        loadFromXML();
    }

    private void loadFromXML() {
        try {
            Document doc = XMLStorageUtil.loadDocument(FILENAME);
            if (doc == null) return;

            NodeList nodes = doc.getElementsByTagName("customer");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element elem = (Element) nodes.item(i);
                String customerId = XMLStorageUtil.getElementText(elem, "customerId");
                String name = XMLStorageUtil.getElementText(elem, "name");
                String phone = XMLStorageUtil.getElementText(elem, "phone");
                String email = XMLStorageUtil.getElementText(elem, "email");
                boolean registered = Boolean.parseBoolean(XMLStorageUtil.getElementText(elem, "registered"));
                int monthlyCount = Integer.parseInt(XMLStorageUtil.getElementText(elem, "monthlyOrderCount"));
                int totalCount = Integer.parseInt(XMLStorageUtil.getElementText(elem, "totalOrderCount"));

                customers.put(phone, new Customer(customerId, name, phone, email, registered, monthlyCount, totalCount));
            }
        } catch (Exception e) {
            System.err. println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveToXML() {
        try {
            Document doc = XMLStorageUtil.createDocument();
            Element root = doc.createElement("customers");
            doc.appendChild(root);

            for (Customer customer : customers.values()) {
                Element elem = doc.createElement("customer");
                XMLStorageUtil.appendElement(doc, elem, "customerId", customer.getCustomerId());
                XMLStorageUtil.appendElement(doc, elem, "name", customer.getName());
                XMLStorageUtil.appendElement(doc, elem, "phone", customer.getPhone());
                XMLStorageUtil.appendElement(doc, elem, "email", customer.getEmail());
                XMLStorageUtil.appendElement(doc, elem, "registered", String.valueOf(customer.isRegistered()));
                XMLStorageUtil. appendElement(doc, elem, "monthlyOrderCount", String.valueOf(customer.getMonthlyOrderCount()));
                XMLStorageUtil.appendElement(doc, elem, "totalOrderCount", String.valueOf(customer.getTotalOrderCount()));
                root.appendChild(elem);
            }

            XMLStorageUtil.saveDocument(doc, FILENAME);
        } catch (Exception e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    public void save(Customer customer) {
        customers. put(customer.getPhone(), customer);
        saveToXML();
    }

    public Customer findByPhone(String phone) {
        return customers.get(phone);
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers. values());
    }

    public void delete(String phone) {
        customers.remove(phone);
        saveToXML();
    }
}