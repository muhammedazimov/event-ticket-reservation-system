package service;

import model.Admin;
import model.Event;
import model.Reservation;
import model.User;

import java.util.List;

public interface IStorageService {


    List<Event> loadEvents();
    void saveEvents(List<Event> events);

    List<Reservation> loadReservations();
    void saveReservations(List<Reservation> reservations, List<Event> currentEvents);

}
