package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.GuestRepository;
import htd.mastery.data.HostRepository;
import htd.mastery.data.ReservationRepository;
import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    public ReservationService(ReservationRepository reservationRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.reservationRepository = reservationRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public List<Reservation> findByHost(Host host) throws DataException {
        return reservationRepository.findByHost(host);
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, true);
        if (!result.isSuccess()) {
            return result;
        }

        reservation.setTotal(calculateTotal(reservation, reservation.getHost()));
        Reservation added = reservationRepository.add(reservation);
        result.setPayload(added);
        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation, false);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setTotal(calculateTotal(reservation, reservation.getHost()));
        boolean success = reservationRepository.update(reservation, reservation.getHost());
        if (!success) {
            result.addMessage("Reservation not found");
        } else {
            result.setPayload(reservation);
        }
        return result;
    }

    public Result<Reservation> cancel(Reservation reservation) throws DataException {
        Result<Reservation> result = new Result<>();
        if (reservation == null) {
            result.addMessage("Reservation is required");
            return result;
        }
        boolean success = reservationRepository.cancel(reservation.getId(), reservation.getHost());
        if (!success) {
            result.addMessage("Reservation not found");
        } else {
            result.setPayload(reservation);
        }
        return result;
    }

    public Result<Reservation> validate(Reservation reservation, boolean isNewReservation) throws DataException {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addMessage("Reservation is required");
            return result;
        }
        //guest and host must be provided
        if (reservation.getGuest() == null || reservation.getHost() == null) {
            result.addMessage("Guest and Host are required");
            return result;
        }

        //start and end date required
        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            result.addMessage("Start date and end date are required");
            return result;
        }
        //start date must come before end date
        if (!reservation.getStartDate().isBefore(reservation.getEndDate())) {
            result.addMessage("Start date must be before end date");
        }
        //for new reservations start date must be in The future
        if (isNewReservation && !reservation.getStartDate().isAfter(LocalDate.now())) {
            result.addMessage("Start date must be in the future");
        }
        //guest must exist
        Guest confirmedGuest = guestRepository.findById(reservation.getGuest().getId());
        if (confirmedGuest == null) {
            result.addMessage("Guest not found");
        }
        //host must exist
        Host confirmedHost = hostRepository.findById(reservation.getHost().getId());
        if (confirmedHost == null) {
            result.addMessage("Host not found");
        }
        //reservations cannot overlap with existing reservations for host
        List<Reservation> existing = reservationRepository.findByHost(reservation.getHost());
        for (Reservation r : existing) {
            boolean isSame = r.getId() == reservation.getId();
            boolean overlaps = !reservation.getStartDate().isBefore(r.getStartDate())
                    && !reservation.getStartDate().isAfter(r.getEndDate());

            if (!isSame && overlaps) {
                result.addMessage("Reservation dates overlap with existing reservation");
            }
        }

        // if valid, update reservation
        if (result.isSuccess()) {
            reservation.setGuest(confirmedGuest);
            reservation.setHost(confirmedHost);
        }

        return result;
    }

    public BigDecimal calculateTotal(Reservation reservation, Host host) throws DataException {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate current = reservation.getStartDate();

        while(!current.isAfter(reservation.getEndDate().minusDays(1))) {
            boolean isWeekend = (current.getDayOfWeek() == DayOfWeek.FRIDAY ||
                    current.getDayOfWeek() == DayOfWeek.SATURDAY);
            BigDecimal rate = isWeekend ? host.getWeekendRate() : host.getStandardRate();
            total = total.add(rate);

            current = current.plusDays(1);
        }
        return total;
    }



}
