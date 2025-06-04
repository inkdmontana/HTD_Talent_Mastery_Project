package htd.mastery.data;

import htd.mastery.models.Host;
import htd.mastery.models.Reservation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {
    ReservationFileRepository repository = new ReservationFileRepository("./data/reservations");

    @Test
    void shouldFindReservationsForValidHost() throws DataException {
        Host host = new Host();
        host.setId("86f374af-ce43-450a-8326-4b9423c9fad7");

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

}