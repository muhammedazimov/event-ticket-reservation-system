package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private int eventId;
    private String eventName;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String location;
    private int totalSeats;
    private int availableSeats;

    public Event(int eventId, String eventName, int year, int month, int day, int hour, int minute, String location, int totalSeats) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.location = location;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }

    public boolean reserveSeats(int numSeats) {
        if (availableSeats >= numSeats) {
            availableSeats -= numSeats;
            return true;
        }
        return false;
    }

    public void cancelSeats(int numSeats) {
        availableSeats += numSeats;
        if (availableSeats > totalSeats) {
            availableSeats = totalSeats;
        }
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getLocation() {
        return location;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Event ID: " + eventId + ", Name: " + eventName +
                ", Date: " + year + "/" + month + "/" + day +
                ", Time: " + hour + ":" + minute +
                ", Location: " + location +
                ", Seats: " + availableSeats + "/" + totalSeats;
    }
    public LocalDateTime getDateTime() {
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public String getFormattedDateTime() {
        return getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
    public void setEventName(String eventName) { this.eventName = eventName; }
    public void setYear(int year) { this.year = year; }
    public void setMonth(int month) { this.month = month; }
    public void setDay(int day) { this.day = day; }
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
    public void setLocation(String location) { this.location = location; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

}