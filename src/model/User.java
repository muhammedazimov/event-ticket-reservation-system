package model;

import service.EventManager;
import service.ReservationManager;

import java.util.List;

public class User {
    protected int id;
    protected String userName;
    protected String firstName;
    protected String lastName;
    protected String email;

    public User(int id, String userName, String firstName, String lastName, String email) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(int id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void reserveTicket(EventManager eventManager, ReservationManager reservationManager, int eventId, int numTickets) {
        Reservation reservation = reservationManager.makeReservation(this, eventId, numTickets, eventManager);
        if (reservation != null) {
            System.out.println("Reservation successful. Reservation ID: " + reservation.getReservationId());
        } else {
            System.out.println("Reservation failed. Not enough available seats.");
        }
    }

    public void cancelReservation(ReservationManager reservationManager, EventManager eventManager, int reservationId) {
        boolean success = reservationManager.cancelReservation(reservationId, eventManager);
        if (success) {
            System.out.println("Reservation cancelled successfully.");
        } else {
            System.out.println("Cancellation failed. Reservation not found.");
        }
    }

    public void viewReservations(ReservationManager reservationManager, EventManager eventManager) {
        List<Reservation> myReservations = reservationManager.getReservationsByUser(getUserName());
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
            } else {
                System.out.println("Reservation ID: " + r.getReservationId() +
                        " | Event: [Event cancelled]" +
                        " | Tickets: " + r.getNumTickets());
            }
        }
    }


    @Override
    public String toString() {
        return "User{id=" + id + ", userName='" + userName + '\'' + '}';
    }
}
