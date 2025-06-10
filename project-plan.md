# 🏠 Don't Wreck My House – Project Plan

## HTD Talent Mastery Project
Tony Vazquez
June 2025



## ⏱️ Project Task List

| Task                                   | Estimate | Actual |
|----------------------------------------|----------|--------|
| Create project plan                    | 4 hr     | 4 hr   |
| Create packages/proj-structure         | 0.5 hr   | 0.5 hr |
| Build models layer                     | 1 hr     | 0.6 hr |
| Build data layer                       | 4 hr     | 4.5 hr |
| Test data layer                        | 2 hr     | 3      |
| Build domain layer                     | 4 hr     | 4 hr   |
| Test domain layer                      | 3 hr     | 3.5 hr |
| Build UI layer                         | 4 hr     | 4 hr   |
| Final testing & polish                 | 1 hr     | 3 hr   |
| Continue with stretch goals if allowed | 4 hr     |        |
| -------------------------------------- | -------- | ------ |







## Class Diagram

![Class Diagram](images/class-diagram.png)


## Class Details 

App:

    public static void main(String[]) -- instantiate all required classes with valid arguments, dependency injection. run controller

data.DataException:
    
    public DataException(String message, Throwable cause)

data.GuestFileRepository:
    
    public Guest findById(int id)
    public List<Guest> findAll()
    private Guest deserialize(String line)
    private String serialize(Guest guest)


data.GuestRepository(interface):

    List<Guest> findAll()
    Guest findById(int id)

data.HostFileRepository:

    public Host findById(String id)
    public List<host> findAll()
    public Host findByEmail(String email)
    private Host deserialize(String line)
    private String serialize(Host host)

data.HostRepository(interface):

    List<Host> findAll()
    Host findById(String id)
    Host findByEmail(String email)    

data.ReservationFileRepository:

    List<Reservation> findByHost(Host host) — retrieves all reservations for a host
    Reservation add(Reservation reservation) — adds a reservation to storage
    boolean update(Reservation reservation) — updates an existing reservation in storage
    boolean cancel(int reservationId) — cancels a reservation

data.ReservationRepository(interface):

    List<Reservation> findByHost(Host host) — retrieves all reservations for a host
    Reservation add(Reservation reservation) — adds a reservation to storage
    boolean update(Reservation reservation) — updates an existing reservation in storage
    boolean cancel(int reservationId) — cancels a reservation

domain.GuestService:
    
    private final GuestRepository repository
    public GuestService(GuestRepository repository)
    public List<Guest> findAll() throws DataException
    public Guest findById(int id)

domain.HostService:

    private final HostRepository repository
    public HostService(HostRepository repository)
    public List<Host> findAll() 
    public Host findById(String id)
    public Host findByEmail(String email)

domain.ReservationService:

    Result<Reservation> add(Reservation reservation) — validates the reservation (dates/overlap), calculates total, and passes it to repository
    Result<Reservation> update(Reservation reservation) — validates and updates a reservation
    Result<Reservation> cancel(int reservationId) — cancels a reservation if valid
    List<Reservation> findByHost(Host host) — retrieves reservations for a host

domain.Result:

    private T payload
    private ArrayList<String> messages
    public boolean isSuccess()
    public void addMessage(String message)
    public List<String> getMessages()
    public void setPayload(T payload)
    public T getPayload()

models.Guest:
    
    private int id
    private String firstName
    private String lastName
    private String email
    private String phone
    private String state

models.Host:

    rivate String id
    private String lastName
    private String email
    private String phone
    private String address
    private String city
    private String state
    private String postalCode
    private BigDecimal standardRate
    private BigDecimal weekendRate

models.Reservation:

    private int id
    private Guest guest
    private Host host
    private LocalDate startDate
    private LocalDate endDate
    private BigDecimal total

    boolean overlaps(LocalDate start, LocalDate end) — returns true if this reservation overlaps a given date range
    BigDecimal calculateTotal() — calculates the total price based on the host’s standard and weekend rates
    boolean isFuture() — returns true if the reservation starts in the future

ui.MainMenuOption(enum):

    enum with 5 options, EXIT, VIEW_RESERVATIONS_FOR_HOST, MAKE_RESERVATION, EDIT_RESERVATION, AND CANCEL_RESERVATION

ui.Controller:

    private final ReservationService reservationService
    private final HostService hostService
    private final GuestService guestService
    private final View view

    public Controller(ReservationService reservationService, HostService hostService, GuestService guestService, View view)
    public void run() — launches the main application loop
    private void runAppLoop() — displays menu and routes user input to appropriate methods
    private void viewReservationsForHost() — handles flow to view reservations by host email
    private void makeReservation() — handles reservation creation with guest and host selection, input validation, and confirmation
    private void editReservation() — handles searching, validating, and updating an existing reservation
    private void cancelReservation() — handles searching and cancelling an existing reservation if it's in the future

ui.View:

    public MainMenuOption selectMainMenuOption()
    public String readRequiredString(String prompt)
    public String readValidEmail(String prompt)
    public LocalDate readDate(String prompt)
    public BigDecimal readBigDecimal(String prompt)
    public void displayHeader(String message)
    public void displayStatus(boolean success, String message)
    public void displayReservations(Host host, List<Reservation> reservations)
    public Reservation chooseReservation(List<Reservation> reservations)
    public boolean confirm(String prompt)


## Project Structure

I prepared for potentially doing stretch goals. 06/03/2025

![Project Structure](images/project-structure.png)
