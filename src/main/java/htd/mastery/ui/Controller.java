package htd.mastery.ui;

import htd.mastery.data.DataException;
import htd.mastery.domain.GuestService;
import htd.mastery.domain.HostService;
import htd.mastery.domain.ReservationService;
import htd.mastery.domain.Result;
import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Controller {
    private final View view;
    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;

    public Controller(View view, GuestService guestService, HostService hostService, ReservationService reservationService) {
        this.view = view;
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
    }

    public void run() {
        view.displayHeader("Dont Wreck My House Mastery App");

        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye!");
    }

    public void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsForHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewReservationsForHost() throws DataException {
        String email = view.readValidEmail("Host email: ");
        Host host = hostService.findByEmail(email);

        if (host == null) {
            view.displayStatus(false, "Host not found");
            return; // ← This is what was missing
        }

        List<Reservation> reservations = reservationService.findByHost(host);

        if (reservations == null || reservations.isEmpty()) {
            view.displayHeader("No Reservations found");
            return;
        }

        view.displayReservations(host, reservations);
    }


    private void makeReservation() throws DataException {
        view.displayHeader("Make a Reservation");

        String guestEmail = view.readValidEmail("Guest email: ");
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest not found");
            return;
        }

        String hostEmail = view.readValidEmail("Host email: ");
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayStatus(false, "Host not found");
            return;
        }

        List<Reservation> existing = reservationService.findByHost(host);
        view.displayReservations(host, existing);

        LocalDate start = view.readDate("Start (MM/dd/yyyy): ");
        LocalDate end = view.readDate("End (MM/dd/yyyy): ");

        Reservation reservation = new Reservation();
        reservation.setGuest(guest);
        reservation.setHost(host);
        reservation.setStartDate(start);
        reservation.setEndDate(end);

        Result<Reservation> result = reservationService.validate(reservation, true);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
            return;
        }

        reservation.setTotal(reservationService.calculateTotal(reservation, host));
        view.displayReservationSummary(reservation);

        if (!view.confirm("Is this okay?")) {
            view.displayHeader( "Reservation cancelled");
            return;
        }

        result = reservationService.add(reservation);
        if (result.isSuccess()) {
            view.displayStatus(true, String.format("Reservation %s created", result.getPayload().getId()));
        } else {
            view.displayStatus(false, result.getMessages());
        }

    }

    private void editReservation() throws DataException {
        view.displayHeader("Edit a Reservation");

        String guestEmail = view.readValidEmail("Guest Email: ");
        Guest guest = guestService.findByEmail(guestEmail);
        if (guest == null) {
            view.displayStatus(false, "Guest not found.");
            return;
        }

        String hostEmail = view.readValidEmail("Host Email: ");
        Host host = hostService.findByEmail(hostEmail);
        if (host == null) {
            view.displayStatus(false, "Host not found.");
            return;
        }

        List<Reservation> reservations = reservationService.findByHost(host);
        if (reservations == null || reservations.isEmpty()) {
            view.displayStatus(false, "No reservations found.");
            return;
        }

        String hostLocationHeader = String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState());
        view.displayHeader(hostLocationHeader);

        view.displayReservations(host, reservations);


        Reservation toEdit = view.chooseReservationById(reservations);
        if (toEdit == null) {
            return;
        }

        view.displayHeader(String.format("Editing Reservation %s", toEdit.getId()));


        LocalDate newStart = view.readDate(String.format(
                "Start (%s): ", toEdit.getStartDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        ));
        LocalDate newEnd = view.readDate(String.format(
                "End (%s): ", toEdit.getEndDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
        ));

        toEdit.setStartDate(newStart);
        toEdit.setEndDate(newEnd);
        toEdit.setTotal(reservationService.calculateTotal(toEdit, host));

        view.displayReservationSummary(toEdit);

        if (!view.confirm("Is this okay?")) {
            view.displayHeader("No changes made.");
            return;
        }

        Result<Reservation> result = reservationService.update(toEdit);
        if (result.isSuccess()) {
            view.displayStatus(true, String.format("Reservation %s updated.", toEdit.getId()));
        } else {
            view.displayStatus(false, result.getMessages());
        }
    }

    private void cancelReservation() throws DataException {

    }
}

