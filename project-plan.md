# 🏠 Don't Wreck My House – Project Plan

## HTD Talent Mastery Project
Tony Vazquez
June 2025



## ⏱️ Project Task List

| Task                                   | Estimate | Actual |
|----------------------------------------|----------|--------|
| Create project plan                    | 4 hr     | 4 hr   |
| Create packages/proj-structure         | 0.5 hr   | 0.5 hr |
| Build models layer                     | 2 hr     | 0.6 hr |
| Build data layer                       | 4 hr     | hr     |
| Test data layer                        | 2 hr     |        |
| Build domain layer                     | 4 hr     | hr     |
| Test domain layer                      | 3 hr     |        |
| Build UI layer                         | 4 hr     |        |
| Final testing & polish                 | 1 hr     |        |
| Continue with stretch goals if allowed | 4 hr     |        |
| -------------------------------------- | -------- | ------ |







## Class Diagram

![Class Diagram](images/class-diagram.png)


## Class Details 

Reservation: 

    boolean overlaps(LocalDate start, LocalDate end) — returns true if this reservation overlaps a given date range
    BigDecimal calculateTotal() — calculates the total price based on the host’s standard and weekend rates
    boolean isFuture() — returns true if the reservation starts in the future

ReservationService:

    Result<Reservation> add(Reservation reservation) — validates the reservation (dates/overlap), calculates total, and passes it to repository
    Result<Reservation> update(Reservation reservation) — validates and updates a reservation
    Result<Reservation> cancel(int reservationId) — cancels a reservation if valid
    List<Reservation> findByHost(Host host) — retrieves reservations for a host

ReservationRepository(Interface):

    List<Reservation> findByHost(Host host) — retrieves all reservations for a host
    Reservation add(Reservation reservation) — adds a reservation to storage
    boolean update(Reservation reservation) — updates an existing reservation in storage
    boolean cancel(int reservationId) — cancels a reservation

ReservationFileRepository:

    List<Reservation> findByHost(Host host) — retrieves all reservations for a host
    Reservation add(Reservation reservation) — adds a reservation to storage
    boolean update(Reservation reservation) — updates an existing reservation in storage
    boolean cancel(int reservationId) — cancels a reservation

## Project Structure

I prepared for potentially doing stretch goals. 06/03/2025

![Project Structure](images/project-structure.png)
