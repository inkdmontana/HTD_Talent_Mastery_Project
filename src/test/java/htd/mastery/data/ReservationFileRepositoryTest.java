package htd.mastery.data;

import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    ReservationFileRepository repository;
    Host host;

    @BeforeEach
    void setup() throws IOException {
        Path seed = Paths.get("src/test/data/reservations/86f374af-ce43-450a-8326-4b9423c9fad7-seed.csv");
        Path test = Paths.get("src/test/data/reservations/86f374af-ce43-450a-8326-4b9423c9fad7.csv");

        Files.copy(seed, test, StandardCopyOption.REPLACE_EXISTING);

        repository = new ReservationFileRepository("src/test/data/reservations");

        host = new Host();
        host.setId("86f374af-ce43-450a-8326-4b9423c9fad7");
    }


    @Test
    void shouldFindReservationsForValidHost() throws DataException {
        List<Reservation> reservations = repository.findByHost(host);

        assertNotNull(reservations);
        assertTrue(!reservations.isEmpty());
    }

    @Test
    void shouldNotFindReservationsForInvalidHost() throws DataException {
        Host host = new Host();
        host.setId("0000000000000000000000000000000000");
        List<Reservation> reservations = repository.findByHost(host);
        assertNotNull(reservations);
        assertTrue(reservations.isEmpty());
    }

    @Test
    void shouldAddReservation() throws DataException {
        List<Reservation> before = repository.findByHost(host);

        Reservation reservation = new Reservation();
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setId(999);
        reservation.setGuest(guest);

        reservation.setStartDate(LocalDate.of(2025, 10,1));
        reservation.setEndDate(LocalDate.of(2025, 10,2));
        reservation.setTotal(new BigDecimal("300.00"));

        Reservation added = repository.add(reservation);
        List<Reservation> after = repository.findByHost(host);
        assertNotNull(added);
        assertEquals(before.size() + 1, after.size());
    }

    @Test
    void shouldUpdateReservation() throws DataException {
        List<Reservation> before = repository.findByHost(host);
        Reservation existing = before.get(0);
        LocalDate newEndDate = existing.getEndDate().plusDays(2);

        existing.setEndDate(newEndDate);
        existing.setTotal(new BigDecimal("1200.00"));

        boolean result = repository.update(existing, host);
        List<Reservation> after = repository.findByHost(host);

        assertTrue(result);
        assertEquals(newEndDate, after.get(0).getEndDate());
        assertEquals(new BigDecimal("1200.00"), after.get(0).getTotal());
    }

    @Test
    void shouldNotUpdateNonexistentReservation() throws DataException {
        Reservation fake = new Reservation();
        fake.setId(99999);
        fake.setHost(host);
        boolean result = repository.update(fake, host);
        assertFalse(result);
    }


    @Test
    void shouldCancelReservation() throws DataException {
        List<Reservation> before = repository.findByHost(host);
        Reservation toCancel = before.get(0);
        int idToCancel = toCancel.getId();

        boolean result = repository.cancel(idToCancel, host);
        List<Reservation> after = repository.findByHost(host);

        assertTrue(result);
        assertEquals(before.size() - 1, after.size());
        assertFalse(after.stream().anyMatch(r -> r.getId() == idToCancel));
    }

    @Test
    void shouldNotCancelReservationWhenIdNotFound() throws DataException {
        boolean result = repository.cancel(99999, host); // some non-existent ID
        assertFalse(result);
    }



}