package htd.mastery.data;

import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {
    private final String directory;
    private final GuestRepository guestRepo;
    private final HostRepository hostRepo;

    public ReservationFileRepository(String directory, GuestRepository guestRepo, HostRepository hostRepo) {
        this.directory = directory;
        this.guestRepo = guestRepo;
        this.hostRepo = hostRepo;
    }

    @Override
    public List<Reservation> findByHost(Host host) throws DataException {
        List<Reservation> result = new ArrayList<>();
        Path path = getFilePath(host);

        if (!Files.exists(path)) {
            return result;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                Reservation r = deserialize(line, host);
                if (r != null) {
                    result.add(r);
                }
            }

        } catch (IOException e) {
            throw new DataException("Could not read reservation file");
        }
        return result;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> reservations = findByHost(reservation.getHost());
        int nextId = getNextId(reservations);

        reservation.setId(nextId);
        reservations.add(reservation);
        writeAll(reservations, reservation.getHost());

        return reservation;
    }
    private int getNextId(List<Reservation> reservations) {
        return reservations.stream().mapToInt(Reservation::getId)
                .max().orElse(0) + 1;
    }

    @Override
    public boolean update(Reservation reservation, Host host) throws DataException {
        List<Reservation> all = findByHost(host);

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeAll(all, host);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancel(int reservationId, Host host) throws DataException {
        List<Reservation> all = findByHost(host);

        boolean removed = all.removeIf(r -> r.getId() == reservationId);
        if (removed) {
            writeAll(all, host);
        }
        return removed;
    }

    private Path getFilePath(Host host) {
        return Paths.get(directory, host.getId() + ".csv");
    }

    private void writeAll(List<Reservation> reservations, Host host) throws DataException {
        Path filePath = getFilePath(host);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(filePath))) {
            writer.println("id,start_date,end_date,guest_id,total");
            for (Reservation r : reservations) {
                writer.println(serialize(r));
            }
        } catch (IOException ex) {
            throw new DataException("Could not read/write file");
        }
    }
    private String serialize(Reservation r) {
        return String.format("%d,%s,%s,%d,%s",
                r.getId(), r.getStartDate(), r.getEndDate(), r.getGuest().getId(), r.getTotal());
    }

    private Reservation deserialize(String line, Host host) throws DataException {
        String[] fields = line.split(",", -1);
        if (fields.length != 5) {
            throw new DataException("Invalid reservation line");
        }
        Reservation r = new Reservation();
        try {
            r.setId(Integer.parseInt(fields[0]));
            r.setStartDate(LocalDate.parse(fields[1]));
            r.setEndDate(LocalDate.parse(fields[2]));

            int guestId = Integer.parseInt(fields[3]);
            Guest guest = guestRepo.findById(guestId);
            if (guest == null) {
                throw new DataException("Guest not found");
            }
            r.setGuest(guest);

            r.setTotal(new BigDecimal(fields[4]));
            r.setHost(host);
        } catch (Exception ex) {
            throw new DataException("Error deserializing reservation");
        }
        return r;
    }

}
