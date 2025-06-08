package htd.mastery.ui;

import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.io.Console;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class View {

    private ConsoleIO io;
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public View(ConsoleIO io) { this.io = io;}

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            if (!option.isHidden()) {
                io.printf("%s. %s%n", option.getValue(), option.getMessage());
            }
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }

        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }


    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayException(Exception e) {
        displayHeader("Critical error");
        io.println(e.getMessage());
    }

    public void enterToContinue() {
        io.readString("Press [enter] to continue");
    }

    public String readValidEmail(String prompt) {
        String email;
        do {
            email = io.readRequiredString(prompt);
            if (!email.matches("^.+@.+\\..+$")) {
                displayStatus(false, "Invalid email format");
            }
        } while (!email.matches("^.+@.+\\..+$"));
        return email;
    }

    public void displayReservations(Host host, List<Reservation> reservations) {
        displayHeader("View Reservations for Host");
        io.printf("Host Email: %s%n", host.getEmail());
        String hostLocationHeader = String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState());
        displayHeader(hostLocationHeader);

        for (Reservation r : reservations) {
            io.printf("ID: %s, %s - %s, Guest: %s, %s, Email: %s%n",
                    r.getId(), r.getStartDate(), r.getEndDate(), r.getGuest().getLastName(),
                    r.getGuest().getFirstName(), r.getGuest().getEmail());
        }
    }

    public void displayReservationSummary(Reservation res) {
        displayHeader("Summary");
        io.printf("Start: %s%n", res.getStartDate().format(dateFormat));
        io.printf("End: %s%n", res.getEndDate().format(dateFormat));
        io.printf("Total: %s%n", currencyFormat.format(res.getTotal()));
    }

    public Reservation chooseReservationById(List<Reservation> reservations) {
        int id = io.readInt("Enter Reservation ID: ");
        for (Reservation res : reservations) {
            if (res.getId() == id) {
                return res;
            }
        }
        io.println("Reservation not found");
        return null;
    }

    public boolean confirm(String prompt) {
        String input = io.readRequiredString(prompt + " [y/n]: ");
        return input.equalsIgnoreCase("y");
    }

    public LocalDate readDate(String prompt) {
        return io.readLocalDate(prompt);
    }



}
