package htd.mastery;

import htd.mastery.data.ReservationFileRepository;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.util.List;

public class App {
    public static void main(String[] args) {
        ReservationFileRepository repo = new ReservationFileRepository("./data/reservations"); // point to your folder
        Host host = new Host();
        host.setId("86f374af-ce43-450a-8326-4b9423c9fad7");

        try {
            List<Reservation> reservations = repo.findByHost(host);
            for (Reservation r : reservations) {
                System.out.println("Reservation ID: " + r.getId());
                System.out.println("Start: " + r.getStartDate());
                System.out.println("End: " + r.getEndDate());
                System.out.println("Guest ID: " + r.getGuest().getId());
                System.out.println("Total: $" + r.getTotal());
                System.out.println("-----------");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
