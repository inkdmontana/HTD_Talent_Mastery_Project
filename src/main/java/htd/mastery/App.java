package htd.mastery;

import htd.mastery.data.*;
import htd.mastery.domain.GuestService;
import htd.mastery.domain.HostService;
import htd.mastery.domain.ReservationService;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;
import htd.mastery.ui.ConsoleIO;
import htd.mastery.ui.Controller;
import htd.mastery.ui.View;

import java.io.Console;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ConsoleIO io = new ConsoleIO();
        View view = new View(io);

        GuestRepository guestRepo = new GuestFileRepository("./data/guests.csv");
        HostRepository hostRepo = new HostFileRepository("./data/hosts.csv");
        ReservationRepository reservationRepo = new ReservationFileRepository("./data/reservations", guestRepo, hostRepo);

        GuestService guestService = new GuestService(guestRepo);
        HostService hostService = new HostService(hostRepo);
        ReservationService reservationService = new ReservationService(reservationRepo, hostRepo, guestRepo);

        Controller controller = new Controller(view, guestService, hostService, reservationService);

        controller.run();
        }
    }