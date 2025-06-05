package htd.mastery.data;

import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationRepositoryDouble implements ReservationRepository {
    private List<Reservation> reservations = new ArrayList<>();

    public ReservationRepositoryDouble() {

    }

    @Override
    public List<Reservation> findByHost(Host host) {
        return reservations.stream().filter(r -> r.getHost().getId().equals(host.getId()))
                .collect(Collectors.toList());
    }

    @Override
    pub
}
