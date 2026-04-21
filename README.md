# CinemaTicketingManagementSystem
A Java-based Object-Oriented Programming project that simulates a cinema ticketing system with seat selection, showtime management, and role-based access for Admin and Cashier users.
# 🎬 Cinema Ticketing Management System

## 📌 Overview
The Cinema Ticketing Management System is a Java-based application developed as part of an Object-Oriented Programming (OOP) project. It simulates real-world cinema operations, allowing administrators to manage movies and showtimes, while cashiers handle ticket sales, seat selection, and payment processing.

This project demonstrates key OOP principles such as encapsulation, inheritance, polymorphism, and abstraction, and may include a Graphical User Interface (GUI) if time permits.

---

## 🎯 Objectives
- Apply Object-Oriented Programming concepts in a real-world scenario
- Design a structured system using UML diagrams (Class Diagram & Use Case Diagram)
- Implement role-based access control (Admin and Cashier)
- Simulate a cinema ticket purchasing workflow

---

## 👥 User Roles

### 🔑 Admin
- Login/Logout
- Full system access
- Manage movies (create, read, update, delete)
- Manage showtimes and theater rooms
- Manage users
- View sales reports
- Can also perform cashier tasks

### 💼 Cashier
- Login/Logout
- Search movies and showtimes
- Select available seats
- Sell tickets
- Process payments
- Generate receipts

---

## ⚙️ Features
- 🎥 Movie Management
- 🕒 Showtime Scheduling
- 💺 Seat Selection & Availability Tracking
- 🎟️ Ticket Selling System
- 💳 Payment Processing
- 🧾 Receipt Generation
- 📊 Sales Reporting (Admin)
- 🔐 Role-Based Access Control

---

## 🧠 OOP Concepts Applied
- **Encapsulation** – Private fields with getters/setters
- **Inheritance** – `Admin` extends `Cashier`, `Cashier` extends `User`
- **Polymorphism** – Flexible handling of behaviors (e.g., payment methods)
- **Abstraction** – Simplified system interactions through classes

---

## 🏗️ System Architecture (Main Classes)

- User
- ├── Admin
- └── Cashier
- Movie
- Showtime
- TheaterRoom
- Seat
- Ticket
- SaleTransaction
- Payment
- Receipt
- ---

## 🔄 System Workflow
1. Admin logs in and configures movies and showtimes
2. Cashier logs in
3. Cashier selects a movie and showtime
4. System displays available seats
5. Cashier assigns seat(s)
6. Cashier processes payment
7. System generates ticket and receipt

---

## 🛠️ Technologies Used
- Java (Core Java / OOP Concepts)
- *(Optional)* Java Swing / JavaFX for GUI
- *(Optional)* File Handling or Database (MySQL)

---

## 📊 UML Diagrams
- ✅ Use Case Diagram
- ✅ Class Diagram

> Diagrams are included in the project documentation folder.

---

## 📅 Project Timeline
This project is developed within a **1-month timeframe**, following structured planning, design, and implementation phases.

---

## 👨‍💻 Team Members
| Name |
|------|
| Agad, Mark Laurence |
| Delos Reyes, Kervin Curt |
| Gerzon, Ythan Erik Jon |
| Merenillo, John Niko |

---

## 🚀 Future Improvements
- Online booking system for customers
- Mobile or web-based interface
- Advanced reporting and analytics
- Integration with payment gateways

---

## 📜 License
This project is for **academic purposes only**.

---

## 💡 Notes
> This system is a simulation of a real-world cinema ticketing process and is designed to demonstrate clean code structure and proper application of OOP principles.

##  Class Diagram
<img width="1072" height="755" alt="Class Diagram" src="https://github.com/user-attachments/assets/6979e508-1ec1-4719-9e5f-8c90a4d1b316" />

## Use-Case Diagram
<img width="1100" height="645" alt="OOP2_UseCase (1)" src="https://github.com/user-attachments/assets/c4b97af6-e4f7-41ff-be34-6d6ecce20a54" />

