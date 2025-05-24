package model;

public class Reservation {
    private int reservationId;
    private String userName;
    private int eventId;
    private int numTickets;

    public Reservation(int reservationId, String userName, int eventId, int numTickets) {
        this.reservationId = reservationId;
        this.userName = userName;
        this.eventId = eventId;
        this.numTickets = numTickets;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getUserName() {
        return userName;
    }

    public int getEventId() {
        return eventId;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", User: " + userName +
                ", Event ID: " + eventId +
                ", Tickets: " + numTickets;
    }
}
