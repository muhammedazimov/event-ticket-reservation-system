package service;

import model.Event;

import java.util.List;

public interface IEventManager {
    void createEvent(String name, int year, int month, int day, int hour, int minute, String location, int totalSeats);
    boolean removeEvent(int eventId);
    boolean updateEvent(int eventId, String name, int year, int month, int day, int hour, int minute, String location, int totalSeats);
    Event findEventById(int eventId);
    List<Event> getAllEvents();
    void updateEvent(Event updatedEvent);
}
