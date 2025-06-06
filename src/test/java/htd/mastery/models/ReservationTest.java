package htd.mastery.models;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void shouldCalculateTotalStandardRateOnly() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("150.00"));
        host.setWeekendRate(new BigDecimal("250.00"));

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2025, 9 , 1)); //Mon
        reservation.setEndDate(LocalDate.of(2025, 9, 5)); //Fri - not inclusive

        BigDecimal expected = new BigDecimal("600.00");
        assertEquals(expected, reservation.calculateTotal());
    }

    @Test
    void shouldCalculateTotalWeekendRateOnly() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("150.00"));
        host.setWeekendRate(new BigDecimal("250.00"));

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2025, 9 , 5)); //Friday
        reservation.setEndDate(LocalDate.of(2025, 9, 7)); //Sun - not inclusive

        BigDecimal expected = new BigDecimal("500.00");
        assertEquals(expected, reservation.calculateTotal());
    }

    @Test
    void shouldCalculateTotalForStandardAndWeekendRate() {
        Host host = new Host();
        host.setStandardRate(new BigDecimal("150.00"));
        host.setWeekendRate(new BigDecimal("250.00"));

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setStartDate(LocalDate.of(2025, 9 , 4)); //thurs
        reservation.setEndDate(LocalDate.of(2025, 9, 7));//sun
        BigDecimal expected = new BigDecimal("650.00");
        assertEquals(expected, reservation.calculateTotal());
    }

    @Test
    void shouldReturnTrueWhenDatesOverlap() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2025, 9 , 1));
        reservation.setEndDate(LocalDate.of(2025, 9, 5));

        assertTrue(reservation.overlaps(LocalDate.of(2025,9,3), LocalDate.of(2025,9,4)));
    }

    @Test
    void shouldReturnFalseWhenDatesNotOverlap() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2025, 9 , 1));
        reservation.setEndDate(LocalDate.of(2025, 9, 5));

        assertFalse(reservation.overlaps(LocalDate.of(2025,9,6), LocalDate.of(2025,9,8)));
    }

    @Test
    void shouldReturnTrueIfStartDateIsFuture() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(1));

        assertTrue(reservation.isFuture());
    }

    @Test
    void shouldReturnFalseIfEndDateIsFuture() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now());

        assertFalse(reservation.isFuture());
    }

}