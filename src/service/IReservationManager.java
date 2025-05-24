package service;

import model.Reservation;
import model.User;

import java.util.List;

public interface IReservationManager {
    Reservation makeReservation(User user, int eventId, int numTickets, EventManager eventManager);
    boolean cancelReservation(int reservationId, EventManager eventManager);
    List<Reservation> getReservationsByUser(String username);

}
