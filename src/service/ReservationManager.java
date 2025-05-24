// ReservationManager.java (güncellenmiş hali)
package service;

import model.Event;
import model.Reservation;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class ReservationManager implements IReservationManager {
    private List<Reservation> reservations;
    private int reservationCounter = 1;
    private final StorageService storageService;

    public ReservationManager(StorageService storageService) {
        this.storageService = storageService;
        this.reservations = storageService.loadReservations();

        for (Reservation r : reservations) {
            reservationCounter = Math.max(reservationCounter, r.getReservationId() + 1);
        }
    }

    public Reservation makeReservation(User user, int eventId, int numTickets, EventManager eventManager) {
        Event event = eventManager.findEventById(eventId);
        if (event == null || event.getAvailableSeats() < numTickets) {
            return null;
        }

        event.reserveSeats(numTickets);
        eventManager.updateEvent(event);

        Reservation reservation = new Reservation(reservationCounter++, user.getUserName(), eventId, numTickets);
        reservations.add(reservation);
        storageService.saveReservations(reservations, eventManager.getAllEvents());


        return reservation;
    }

    public boolean cancelReservation(int reservationId, EventManager eventManager) {
        Reservation target = null;
        for (Reservation r : reservations) {
            if (r.getReservationId() == reservationId) {
                target = r;
                break;
            }
        }

        if (target == null) return false;

        Event event = eventManager.findEventById(target.getEventId());
        if (event != null) {
            event.cancelSeats(target.getNumTickets());
            eventManager.updateEvent(event);
        }

        reservations.remove(target);
        storageService.saveReservations(reservations, eventManager.getAllEvents());

        return true;
    }

    public List<Reservation> getReservationsByUser(String username) {
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getUserName().equals(username)) {
                userReservations.add(r);
            }
        }
        return userReservations;
    }

}
