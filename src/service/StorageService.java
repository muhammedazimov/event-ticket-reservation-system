// StorageService.java (güncellenmiş hali)
package service;

import model.Admin;
import model.Event;
import model.Reservation;
import model.User;

import java.io.*;
import java.util.*;

public class StorageService implements IStorageService {
    private static final String EVENTS_FILE = "data/events_unreserved.txt";
    private static final String RESERVATIONS_FILE = "data/reservations.txt";

    public StorageService() {
        new File("data").mkdirs();
    }


    // EVENTS
    public void saveEvents(List<Event> events) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EVENTS_FILE))) {
            writer.write("id|name|year|month|day|hour|minute|location|totalSeats|availableSeats");
            writer.newLine();
            for (Event e : events) {
                writer.write(e.getEventId() + "|" + e.getEventName() + "|" +
                        e.getYear() + "|" + e.getMonth() + "|" + e.getDay() + "|" +
                        e.getHour() + "|" + e.getMinute() + "|" +
                        e.getLocation() + "|" + e.getTotalSeats() + "|" + e.getAvailableSeats());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Event> loadEvents() {
        List<Event> events = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(EVENTS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length >= 10) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    int year = Integer.parseInt(parts[2]);
                    int month = Integer.parseInt(parts[3]);
                    int day = Integer.parseInt(parts[4]);
                    int hour = Integer.parseInt(parts[5]);
                    int minute = Integer.parseInt(parts[6]);
                    String location = parts[7];
                    int total = Integer.parseInt(parts[8]);
                    int available = Integer.parseInt(parts[9]);

                    Event e = new Event(id, name, year, month, day, hour, minute, location, total);
                    e.setAvailableSeats(available);
                    events.add(e);
                }
            }
        } catch (IOException e) {
        }
        return events;
    }


    // RESERVATIONS
    public void saveReservations(List<Reservation> reservations, List<Event> currentEvents) {
        Map<Integer, String> eventNameMap = new HashMap<>();
        for (Event e : currentEvents) {
            eventNameMap.put(e.getEventId(), e.getEventName());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/reservations.txt"))) {
            writer.write("id|username|eventId|numTickets");
            writer.newLine();
            for (Reservation r : reservations) {
                String eventInfo = eventNameMap.containsKey(r.getEventId())
                        ? r.getEventId() + "(" + eventNameMap.get(r.getEventId()) + ")"
                        : "Cancelled(" + r.getEventId() + ")";
                writer.write(r.getReservationId() + "|" + r.getUserName() + "|" + eventInfo + "|" + r.getNumTickets());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Reservation> loadReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RESERVATIONS_FILE))) {
            String line;
            boolean first = true;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String user = parts[1];
                    String eventRaw = parts[2];
                    int eventId;
                    try {
                        eventId = Integer.parseInt(eventRaw.replaceAll("\\D.*", ""));
                    } catch (NumberFormatException e) {
                        continue; // bozuksa satırı atla
                    }
                    int tickets = Integer.parseInt(parts[3]);
                    reservations.add(new Reservation(id, user, eventId, tickets));
                }
            }
        } catch (IOException e) {
        }
        return reservations;
    }

}
