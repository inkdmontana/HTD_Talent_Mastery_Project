package htd.mastery.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;


public class Reservation {
    private int id;
    private Guest guest;
    private Host host;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal total;

    public Reservation() {
    }

    public Reservation(int id, Guest guest, Host host, LocalDate startDate, LocalDate endDate, BigDecimal total) {
        this.id = id;
        this.guest = guest;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean overlaps(LocalDate start, LocalDate end) {
        return !this.startDate.isAfter(end) && !this.endDate.isBefore(start);
    }

    public BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        LocalDate current = startDate;

        while(!current.isAfter(endDate.minusDays(1))) {
            boolean isWeekend = (current.getDayOfWeek() == DayOfWeek.FRIDAY ||
                                current.getDayOfWeek() == DayOfWeek.SATURDAY);
            BigDecimal rate = isWeekend ? host.getWeekendRate() : host.getStandardRate();
            total = total.add(rate);

            current = current.plusDays(1);
        }
        return total;
    }

    public boolean isFuture() {
        return startDate.isAfter(LocalDate.now());
    }
}
