// Main.java
import model.Admin;
import model.Event;
import model.Reservation;
import model.User;
import service.AuthService;
import service.EventManager;
import service.ReservationManager;
import service.StorageService;

import java.util.*;

public class Main {
    private static final String ADMIN_SECRET = "1111";
    private static final Scanner scanner = new Scanner(System.in);
    private static final StorageService storageService = new StorageService();
    private static final EventManager eventManager = new EventManager(storageService);
    private static final ReservationManager reservationManager = new ReservationManager(storageService);
    private static final AuthService authService = new AuthService();

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to Event Ticket Reservation System");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int option = readInt();

            switch (option) {
                case 1 -> login();
                case 2 -> register();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (authService.isUsernameTaken(username)) {
            Object user = authService.login(username, password);
            if (user instanceof Admin admin) {
                adminMenu(admin);
            } else if (user instanceof User u) {
                userMenu(u);
            } else {
                while (true) {
                    System.out.println("Incorrect password. Try again or type 'cancel' to exit:");
                    System.out.print("Password: ");
                    String retryPassword = scanner.nextLine().trim();
                    if (retryPassword.equalsIgnoreCase("cancel")) {
                        System.out.println("Login cancelled.");
                        return;
                    }
                    Object retryUser = authService.login(username, retryPassword);
                    if (retryUser instanceof Admin adminRetry) {
                        adminMenu(adminRetry);
                        return;
                    } else if (retryUser instanceof User userRetry) {
                        userMenu(userRetry);
                        return;
                    }
                }
            }
        } else {
            System.out.println("No such user found.");
        }
    }

    private static void register() {
        System.out.print("Are you registering as an Admin or User? (admin/user): ");
        String role;
        while (true) {
            role = scanner.nextLine().trim().toLowerCase();
            if (role.equals("admin") || role.equals("user")) break;
            System.out.print("Invalid input. Please enter 'admin' or 'user': ");
        }

        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        if (authService.isUsernameTaken(username)) {
            System.out.println("This username is already taken. Registration failed.");
            return;
        }

        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();
        String firstName = null;
        String lastName = null;
        String email = null;

        if (role.equals("user")) {
            System.out.print("First Name: ");
            firstName = scanner.nextLine().trim();
            System.out.print("Last Name: ");
            lastName = scanner.nextLine().trim();
            System.out.print("Email: ");
            email = scanner.nextLine().trim();
        }

        if (role.equals("admin")) {
            while (true) {
                System.out.print("Enter admin verification password (or type 'cancel' to abort): ");
                String verification = scanner.nextLine().trim();
                if (verification.equalsIgnoreCase("cancel")) {
                    System.out.println("Admin registration cancelled.");
                    return;
                }
                if (verification.equals(ADMIN_SECRET)) {
                    Admin newAdmin = authService.registerNewAdmin(username, password);
                    System.out.println("Admin account created.");
                    adminMenu(newAdmin);
                    break;
                } else {
                    System.out.println("Incorrect verification password. Try again or type 'cancel'.");
                }
            }
        } else {
            User newUser = authService.registerNewUser(username, password, firstName, lastName, email);
            System.out.println("User account created.");
            userMenu(newUser);
        }
    }

    private static void userMenu(User user) {
        while (true) {
            System.out.println("\nUser Menu:");
            System.out.println("1. View Events");
            System.out.println("2. Reserve Ticket");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View My Reservations");
            System.out.println("5. Log Out");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> viewEvents();
                case 2 -> reserveTicket(user);
                case 3 -> cancelReservation(user);
                case 4 -> user.viewReservations(reservationManager, eventManager);
                case 5 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewEvents() {
        System.out.println("\nAvailable Events:");
        List<Event> events = eventManager.getAllEvents();
        events.sort(Comparator.comparing(Event::getDateTime));
        if (events.isEmpty()) {
            System.out.println("No events found.");
        } else {
            for (Event e : events) {
                System.out.println("ID: " + e.getEventId() +
                        " | Name: " + e.getEventName() +
                        " | Date: " + e.getFormattedDateTime() +
                        " | Available Seats: " + e.getAvailableSeats());
            }
        }
    }

    private static void reserveTicket(User user) {
        viewEvents();
        System.out.print("Enter Event ID: ");
        int eventId = readInt();
        Event selected = eventManager.findEventById(eventId);
        if (selected == null) {
            System.out.println("Invalid Event ID.");
            return;
        }
        System.out.print("Number of Tickets: ");
        int num = readInt();
        if (num <= 0 || num > selected.getAvailableSeats()) {
            System.out.println("Invalid ticket count.");
            return;
        }
        user.reserveTicket(eventManager, reservationManager, eventId, num);
    }

    private static void cancelReservation(User user) {
        List<Reservation> myReservations = reservationManager.getReservationsByUser(user.getUserName());
        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }
        System.out.println("Your Reservations:");
        for (Reservation r : myReservations) {
            Event e = eventManager.findEventById(r.getEventId());
            if (e != null) {
                System.out.println("Reservation ID: " + r.getReservationId() +
                        " | Event: " + e.getEventName() +
                        " | Date: " + e.getFormattedDateTime() +
                        " | Tickets: " + r.getNumTickets());
            }
        }
        System.out.print("Enter Reservation ID to cancel: ");
        int reservationId = readInt();
        user.cancelReservation(reservationManager, eventManager, reservationId);
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View Events");
            System.out.println("2. Add Event");
            System.out.println("3. Update Event");
            System.out.println("4. Delete Event");
            System.out.println("5. Log Out");
            System.out.print("Choose: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> admin.viewEvents(eventManager);
                case 2 -> adminAddEvent(admin);
                case 3 -> {
                    viewEvents(); // ✅ Tüm eventleri önce göster
                    adminUpdateEvent(admin);
                }

                case 4 -> {
                    viewEvents(); // ✅ Tüm eventleri önce göster
                    System.out.print("Event ID to delete: ");
                    int eventId = readInt();
                    admin.deleteEvent(eventManager, eventId);
                }

                case 5 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminAddEvent(Admin admin) {
        System.out.print("Event Name: ");
        String name = scanner.nextLine();
        System.out.print("Location: ");
        String location = scanner.nextLine();
        int seats = readPositive("Total Seats");
        int year = readPositive("Year");
        int month = readRanged("Month (1-12)", 1, 12);
        int day = readRanged("Day (1-31)", 1, 31);
        int hour = readRanged("Hour (0-23)", 0, 23);
        int minute = readRanged("Minute (0-59)", 0, 59);
        admin.addEvent(eventManager, name, year, month, day, hour, minute, location, seats);
    }

    private static void adminUpdateEvent(Admin admin) {
        System.out.print("Event ID to update: ");
        int eventId = readInt();
        System.out.print("New Name: ");
        String name = scanner.nextLine();
        System.out.print("New Location: ");
        String location = scanner.nextLine();
        int seats = readPositive("New Total Seats");
        int year = readPositive("Year");
        int month = readRanged("Month (1-12)", 1, 12);
        int day = readRanged("Day (1-31)", 1, 31);
        int hour = readRanged("Hour (0-23)", 0, 23);
        int minute = readRanged("Minute (0-59)", 0, 59);
        admin.updateEvent(eventManager, eventId, name, year, month, day, hour, minute, location, seats);
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private static int readPositive(String label) {
        int value;
        while (true) {
            System.out.print(label + ": ");
            value = readInt();
            if (value > 0) return value;
            System.out.println(label + " must be a positive number.");
        }
    }

    private static int readRanged(String label, int min, int max) {
        int value;
        while (true) {
            System.out.print(label + ": ");
            value = readInt();
            if (value >= min && value <= max) return value;
            System.out.println(label + " must be between " + min + " and " + max + ".");
        }
    }
}