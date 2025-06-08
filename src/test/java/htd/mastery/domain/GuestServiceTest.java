package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.GuestRepositoryDouble;
import htd.mastery.models.Guest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldFindGuestById() throws DataException {
        Guest guest = service.findById(44);
        assertNotNull(guest);
        assertEquals("Georgina", guest.getFirstName());
    }

    @Test
    void shouldReturnNullForUnknownGuestId() throws DataException {
        Guest guest = service.findById(999);
        assertNull(guest);
    }

    @Test
    void shouldFindAllGuests() throws DataException {
        List<Guest> guests = service.findAll();
        assertNotNull(guests);
        assertTrue(guests.size() > 0);
    }


}
