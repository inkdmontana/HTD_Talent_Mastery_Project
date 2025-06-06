package htd.mastery.data;

import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {

    public static final Host HOST = makeHost();
    public static final Guest GUEST = makeGuest();
    private List<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {
        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setHost(HOST);
        reservation.setGuest(GUEST);
        reservation.setStartDate(LocalDate.of(2025, 10, 1)); //wed
        reservation.setEndDate(LocalDate.of(2025, 10, 3)); //friday
        reservation.setTotal(new BigDecimal("300.00"));
        reservations.add(reservation);


    }

    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations.stream().filter(r -> r.getHost().getId().equals(host.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Reservation add(Reservation reservation) {
        reservation.setId(reservations.size() + 1);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation, Host host) {
        for (int i = 0; i<reservations.size(); i++) {
            if (reservations.get(i).getId() == reservation.getId()) {
                reservations.set(i, reservation);
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean cancel(int reservationId, Host host) {
        return reservations.removeIf(r -> r.getId() == reservationId);
    }

    private static Host makeHost() {
        Host host = new Host();
        host.setId("host-abc-123");
        host.setLastName("Doe");
        host.setEmail("host@example.com");
        host.setPhone("(123) 4567890");
        host.setAddress("123 Main St.");
        host.setCity("New York City");
        host.setState("NY");
        host.setPostalCode("12345");
        host.setStandardRate(new BigDecimal("150.00"));
        host.setWeekendRate(new BigDecimal("250.00"));
        return host;
    }

    private static Guest makeGuest() {
        Guest guest = new Guest();
        guest.setId(1);
        guest.setFirstName("Jane");
        guest.setLastName("Test");
        guest.setEmail("guest@example.com");
        guest.setPhone("(987) 6543210");
        guest.setState("FL");
        return guest;

    }
}
