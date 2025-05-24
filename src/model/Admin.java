package model;

import service.EventManager;

public class Admin extends User {

    public Admin(int id, String userName) {
        super(id, userName);
    }

    public void addEvent(EventManager eventManager, String name, int year, int month, int day, int hour, int minute, String location, int totalSeats) {
        eventManager.createEvent(name, year, month, day, hour, minute, location, totalSeats);
        System.out.println("Event created successfully.");
    }

    public void updateEvent(EventManager eventManager, int eventId, String name, int year, int month, int day, int hour, int minute, String location, int totalSeats) {
        boolean result = eventManager.updateEvent(eventId, name, year, month, day, hour, minute, location, totalSeats);
        if (result) {
            System.out.println("Event updated.");
        } else {
            System.out.println("Event not found.");
        }
    }

    public void deleteEvent(EventManager eventManager, int eventId) {
        boolean result = eventManager.removeEvent(eventId);
        if (result) {
            System.out.println("Event deleted.");
        } else {
            System.out.println("Event not found.");
        }
    }

    public void viewEvents(EventManager eventManager) {
        for (Event e : eventManager.getAllEvents()) {
            System.out.println(e);
        }
    }
}