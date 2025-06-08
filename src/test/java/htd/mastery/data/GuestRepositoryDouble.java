package htd.mastery.data;

import htd.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private final Guest GUEST;

    public GuestRepositoryDouble() {
        GUEST = new Guest();
        GUEST.setId(44);
        GUEST.setFirstName("Georgina");
        GUEST.setLastName("Praten");
        GUEST.setEmail("gpraten17@rediff.com");
        GUEST.setPhone("(901) 1754410");
        GUEST.setState("TN");
    }

    @Override
    public List<Guest> findAll() throws DataException {
        List<Guest> result = new ArrayList<>();
        result.add(GUEST);
        return result;
    }

    @Override
    public Guest findById(int id) {
        return GUEST.getId() == id ? GUEST : null;
    }

    @Override
    public Guest findByEmail(String email) {
        return GUEST.getEmail().equalsIgnoreCase(email) ? GUEST : null;
    }

}
