import model.Admin;
import model.Event;
import model.Reservation;
import model.User;
import service.*;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        StorageService storageService = new StorageService();
        EventManager eventManager = new EventManager(storageService);
        ReservationManager reservationManager = new ReservationManager(storageService);
        AuthService authService = new AuthService();

        // 1. Admin oluştur ve etkinlik ekle
        Admin admin = authService.registerNewAdmin("admin1", "pass123");
        eventManager.createEvent("Jazz Concert", 2025, 6, 20, 20, 0, "Hall A", 100);
        eventManager.createEvent("Theatre Play", 2025, 7, 5, 18, 30, "Stage B", 50);

        System.out.println("\n--- All Events ---");
        for (Event e : eventManager.getAllEvents()) {
            System.out.println(e);
        }

        // 2. Kullanıcı oluştur ve rezervasyon yap
        User user = authService.registerNewUser("user1", "1234", "Ali", "Kaya", "ali@example.com");
        Event jazz = eventManager.findEventById(1);
        reservationManager.makeReservation(user, jazz.getEventId(), 2, eventManager);

        // 3. Kullanıcı rezervasyonlarını görüntüle
        System.out.println("\n--- User Reservations ---");
        user.viewReservations(reservationManager, (EventManager) eventManager);

        // 4. Admin etkinlik silsin
        System.out.println("\n--- Deleting Event ID 1 (Jazz Concert) ---");
        eventManager.removeEvent(1);

        // 5. Tekrar rezervasyonları görüntüle
        System.out.println("\n--- User Reservations After Deletion ---");
        user.viewReservations(reservationManager, (EventManager) eventManager);

        // 6. Dosyaları kontrol et: events.txt, reservations.txt içinde ne var
        System.out.println("\n--- Final Event List ---");
        for (Event e : eventManager.getAllEvents()) {
            System.out.println(e);
        }
    }
}
