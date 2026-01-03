# TasteBuds Catering System (TBCS)

> **This project is submitted as Quiz 2 for the course SWE4301.**
>
> ## **admin login password ==> admin123

## Overview

TasteBuds Catering System (TBCS) is a restaurant management and order tracking system designed as a university assignment for SWE4301 (Quiz 2).  
The project is written in Java and demonstrates clean code, application of SOLID principles, and domain-driven design.

## Features

- **Customer Management**
  - Register customers and manage order history
  - Automatic discount calculation for returning customers

- **Order Placement**
  - Add and manage menu items for each order
  - Assign queue numbers and track current serving order

- **Kitchen Preparation (Head Chef)**
  - Orders sent to kitchen and assigned to available chefs
  - Support for normal and priority (urgent) orders
  - Estimated preparation time set by Head Chef

- **Delivery Assignment**
  - Assign available drivers and vehicles to orders
  - Priority orders ensure delivery assignment within 10 minutes
  - Driver validity based on license status

- **Driver Checkout**
  - Drivers must verify with license to pick up order

- **Customer Feedback**
  - Rate delivered orders (1–5 stars)
  - Leave suggestions/comments

## Architecture

- **Languages & Tools:** Java, JUnit, XML
- **SOLID Principles**
  - ☐ **Single Responsibility**: Each class/component does only one thing
  - ☐ **Open-Closed**: Code is ready for extension without modifying existing logic
- **Clean code package structure:**
  - `model/` — Entities (Order, Customer, MenuItem, etc.)
  - `repository/` — XML persistence for each resource
  - `service/` — Business logic/services (OrderService, DeliveryService, etc.)
  - `util/` — Utilities, e.g., XML helpers

## Data Storage

- All data (customers, orders, drivers, vehicles, chefs) is saved in XML for easy testing and demonstration.

## How to Run

1. Ensure you have Java 17+ installed.
2. Compile and run project using your IDE or `mvn clean install` (for Maven).
3. Tests can be found under `src/test/java`.
4. Data is persisted and shared via XML files in the `/data` folder.

## Project Status

- **Assignment submitted:** Yes, for SWE4301 Quiz 2  
- **Main requirements covered:** ✔  
- **SRP and clean code standards followed:** ✔  

## Credits

- **Author:** S.M. Samiul Hossain(GitHub)
- **ID:** 230042110
- **Course:** SWE-4301 (Quiz 2 Assignment)
- **Instructor:** Lutfun Nahar Lota
