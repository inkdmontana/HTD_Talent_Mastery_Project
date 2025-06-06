package htd.mastery.data;

import htd.mastery.models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepositoryDouble implements GuestRepository {

    private final Guest GUEST;

    public GuestRepositoryDouble() {
        GUEST = new Guest();
        GUEST.setId(1);
        GUEST.setFirstName("Jane");
        GUEST.setLastName("Test");
        GUEST.setEmail("guest@example.com");
        GUEST.setPhone("(987) 6543210");
        GUEST.setState("FL");
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

}
