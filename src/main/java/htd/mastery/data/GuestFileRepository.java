package htd.mastery.data;

import htd.mastery.models.Guest;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepository implements GuestRepository{
    private final String filePath;

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Guest findById(int id) {
        try {
            return findAll().stream()
                    .filter(g -> g.getId() == id)
                    .findFirst()
                    .orElse(null);
        } catch (DataException e) {
            return null;
        }
    }

    @Override
    public List<Guest> findAll() throws DataException {
        List<Guest> guests = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                Guest guest = deserialize(line);
                if (guest != null) {
                    guests.add(guest);
                }
            }
        } catch (IOException ex) {
            throw new DataException("Could not read guest data", ex);
        }

        return guests;
    }

    @Override
    public Guest findByEmail(String email) throws DataException {
        return findAll().stream().filter(g -> g.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    private Guest deserialize(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length < 6) return null;

        Guest guest = new Guest();
        guest.setId(Integer.parseInt(fields[0]));
        guest.setFirstName(fields[1].replace("@@@", ","));
        guest.setLastName(fields[2].replace("@@@", ","));
        guest.setEmail(fields[3]);
        guest.setPhone(fields[4]);
        guest.setState(fields[5]);

        return guest;
    }

    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s",
                guest.getId(),
                guest.getFirstName().replace(",", "@@@"),
                guest.getLastName().replace(",", "@@@"),
                guest.getEmail(),
                guest.getPhone(),
                guest.getState());
    }




}
