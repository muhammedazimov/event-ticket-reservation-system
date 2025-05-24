// EventManager.java (güncellenmiş hali)
package service;

import model.Event;
import model.Reservation;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements IEventManager {
    private List<Event> events;
    private int eventCounter = 1;
    private final StorageService storageService;

    public EventManager(StorageService storageService) {
        this.storageService = storageService;
        this.events = storageService.loadEvents();

        for (Event e : events) {
            eventCounter = Math.max(eventCounter, e.getEventId() + 1);
        }
    }

    public void createEvent(String name, int year, int month, int day, int hour, int minute, String location, int totalSeats) {
        Event event = new Event(eventCounter++, name, year, month, day, hour, minute, location, totalSeats);
        events.add(event);
        storageService.saveEvents(events);
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public Event findEventById(int eventId) {
        for (Event e : events) {
            if (e.getEventId() == eventId) {
                return e;
            }
        }
        return null;
    }

    public void updateEvent(Event updatedEvent) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getEventId() == updatedEvent.getEventId()) {
                events.set(i, updatedEvent);
                break;
            }
        }
        storageService.saveEvents(events);
    }

    public boolean updateEvent(int eventId, String name, int year, int month, int day, int hour, int minute, String location, int totalSeats) {
        Event event = findEventById(eventId);
        if (event == null) {
            return false;
        }

        event.setEventName(name);
        event.setYear(year);
        event.setMonth(month);
        event.setDay(day);
        event.setHour(hour);
        event.setMinute(minute);
        event.setLocation(location);
        event.setTotalSeats(totalSeats);

        if (event.getAvailableSeats() > totalSeats) {
            event.setAvailableSeats(totalSeats);
        }

        updateEvent(event);
        return true;
    }
    public boolean removeEvent(int eventId) {
        Event event = findEventById(eventId);
        if (event == null) {
            return false;
        }
        events.remove(event);
        storageService.saveEvents(events);

        // ✅ Reservation dosyasını güncelle
        List<Reservation> allReservations = storageService.loadReservations();
        storageService.saveReservations(allReservations, events); // mevcut event listesiyle yeniden yaz

        return true;
    }

    public void printAllEvents() {
        if (events.isEmpty()) {
            System.out.println("No events available.");
        } else {
            for (Event e : events) {
                System.out.println(e);
            }
        }
    }


}