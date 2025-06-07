package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.GuestRepositoryDouble;
import htd.mastery.data.HostRepositoryDouble;
import htd.mastery.data.ReservationRepositoryDouble;
import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service;

    @BeforeEach
    void setUp() {
        service = new ReservationService(
                new ReservationRepositoryDouble(),
                new HostRepositoryDouble(),
                new GuestRepositoryDouble()
        );
    }

    private Reservation makeValidReservation() throws DataException {
        Reservation reservation = new Reservation();
        Host host = new HostRepositoryDouble().findAll().get(0);
        Guest guest = new GuestRepositoryDouble().findAll().get(0);
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2025, 9, 4));
        reservation.setEndDate(LocalDate.of(2025, 9, 8));
        return  reservation;

    }

    @Test
    void shouldAddReservation() throws DataException {
        Reservation reservation = makeValidReservation();
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(new BigDecimal("800.00"), result.getPayload().getTotal());
    }

    @Test
    void shouldNotAddNullReservation() throws DataException {
        Result<Reservation> result = service.add(null);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldNotAddWithNullDates() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(null);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

        reservation.setStartDate(LocalDate.now().plusDays(1));
        reservation.setEndDate(null);
        result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenStartDateIsAfterEndDate() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 10));
        reservation.setEndDate(LocalDate.of(2025, 9, 5));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithPastStartDate() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.now().minusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(1));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNonexistentGuest() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.getGuest().setId(999); // not in GuestRepositoryDouble
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWithNonexistentHost() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.getHost().setId("not-a-real-id"); // not in HostRepositoryDouble
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddOverlappingReservation() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 10, 2)); // overlaps with test double
        reservation.setEndDate(LocalDate.of(2025, 10, 4));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddWithSingleWeekday() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 4)); // Thursday
        reservation.setEndDate(LocalDate.of(2025, 9, 5));
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("150.00"), result.getPayload().getTotal());
    }

    @Test
    void shouldAddReservationWithSingleWeekendDay() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 6)); // Saturday
        reservation.setEndDate(LocalDate.of(2025, 9, 7));
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("250.00"), result.getPayload().getTotal());
    }

    @Test
    void shouldAddReservationSpanningFullWeek() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 1)); // Monday
        reservation.setEndDate(LocalDate.of(2025, 9, 8));   // Next Monday
        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(new BigDecimal("1250.00"), result.getPayload().getTotal());
    }

    @Test
    void shouldNotAddReservationWithNull() throws DataException {
        Result<Reservation> result = service.add(null);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation is required"));
    }

    @Test
    void shouldNotAddIfGuestIsNull() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setGuest(null);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest and Host are required"));
    }

    @Test
    void shouldNotAddIfHostIsNull() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setHost(null);
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest and Host are required"));
    }

    @Test
    void shouldNotAddIfStartDateEqualsEndDate() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 5));
        reservation.setEndDate(LocalDate.of(2025, 9, 5));
        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be before end date"));
    }

    @Test
    void shouldNotCancelReservationWithInvalidId() throws DataException {
        Reservation invalid = makeValidReservation();
        invalid.setId(9999); // ID doesn't exist in ReservationRepositoryDouble
        Result<Reservation> result = service.cancel(invalid);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Reservation not found"));
    }

    @Test
    void shouldNotAddReservationStartingInPast() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.now().minusDays(1));
        reservation.setEndDate(LocalDate.now().plusDays(2));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be in the future"));
    }

    @Test
    void shouldNotAddReservationStartingToday() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.now());
        reservation.setEndDate(LocalDate.now().plusDays(2));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be in the future"));
    }

    @Test
    void shouldNotAddReservationWithEndDateBeforeStartDate() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 10));
        reservation.setEndDate(LocalDate.of(2025, 9, 5));

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be before end date"));
    }

    @Test
    void shouldNotAddReservationWithZeroNights() throws DataException {
        Reservation reservation = makeValidReservation();
        reservation.setStartDate(LocalDate.of(2025, 9, 5));
        reservation.setEndDate(LocalDate.of(2025, 9, 5)); // same day

        Result<Reservation> result = service.add(reservation);

        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date must be before end date"));
    }









}