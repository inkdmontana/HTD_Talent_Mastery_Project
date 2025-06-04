package htd.mastery.data;

import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findByHost(Host host) throws DataException;
    Reservation add(Reservation reservation);
    boolean update(Reservation reservation, Host host);
    boolean cancel(int reservationId, Host host);
}
