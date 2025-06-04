package htd.mastery.data;

import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import htd.mastery.models.Reservation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository {
    private final String directory;

    public ReservationFileRepository(String directory) {
        this.directory = directory;
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
    public Reservation add(Reservation reservation) {
        return null;
    }

    @Override
    public boolean update(Reservation reservation, Host host) {
        return false;
    }

    @Override
    public boolean cancel(int reservationId, Host host) {
        return false;
    }

    private Path getFilePath(Host host) {
        return Paths.get(directory, host.getId() + ".csv");
    }

    private void writeAll(List<Reservation> reservations, Host host) throws IOException, DataException {
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
            Guest guest = new Guest();
            guest.setId(guestId);
            r.setGuest(guest);

            r.setTotal(new BigDecimal(fields[4]));
            r.setHost(host);
        } catch (Exception ex) {
            throw new DataException("Error deserializing reservation");
        }
        return r;
    }

}
