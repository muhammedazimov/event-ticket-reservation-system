# 🎟️ Event Ticket Reservation System

A Java-based command-line application for managing event ticket reservations. Users can register, log in, view events, and reserve or cancel tickets. Admins can manage events. All data is stored in plain text files.

---

## 🚀 Features

### 👥 User Functionality
- Register and log in
- View available events
- Reserve tickets for an event
- Cancel reserved tickets
- Canceled events appear as `[Event cancelled]` in reservations

### 🛠️ Admin Functionality
- Add new events
- Update existing events
- Delete events
- List all events

### 💾 Data Storage
- `users.txt`, `admins.txt`: user and admin info
- `events_unreserved.txt`: list of all current events
- `reservations.txt`: all reservations (even if the event is deleted)

If an event is deleted, its ID is preserved like: `Cancelled(1)`

---

## 🧠 OOP Principles Applied
- **Encapsulation**: Private fields with accessors
- **Abstraction**: Service classes abstract file operations
- **Inheritance**: `Admin` extends `User`
- **Polymorphism**: `login()` returns `Object` (can be `User` or `Admin`)
- **Interfaces**: `IStorageService`, `IEventManager`, `IReservationManager`, `IAuthService`
- **Single Responsibility**: Each class has one responsibility
- **Modular & reusable code**

---

## 🛠️ Setup & Run

### 1. Compile:
```bash
javac -d out -sourcepath src src/TestApp.java
```

### 2. Run:
```bash
java -cp out TestApp
```

### 3. Ensure required folders exist:
```bash
mkdir data
touch data/users.txt data/admins.txt data/events_unreserved.txt data/reservations.txt
```

> Alternatively, the system creates missing files automatically.

---

## 🧪 Test Scenario (`TestApp.java`)
- Admin creates two events
- User reserves tickets
- Admin deletes an event
- User’s reservation still shown as `[Event cancelled]`
- Final event and reservation states are printed

---

## 📁 Project Structure

```
src/
├── model/
│   ├── Admin.java
│   ├── User.java
│   ├── Event.java
│   └── Reservation.java
├── service/
│   ├── AuthService.java
│   ├── EventManager.java
│   ├── IEventManager.java
│   ├── IReservationManager.java
│   ├── IStorageService.java
│   └── StorageService.java
├── TestApp.java
data/
├── users.txt
├── admins.txt
├── events_unreserved.txt
└── reservations.txt
```

---

## 📌 Notes
- The system is file-based, no database used
- Admin registration requires secret input
- All reservation history is preserved, even for deleted events

---

## 📃 License
This project is built for educational purposes.
