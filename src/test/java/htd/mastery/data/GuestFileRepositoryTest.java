package htd.mastery.data;

import htd.mastery.models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepositoryTest {

    GuestFileRepository repository;

    @BeforeEach
    void setup() throws IOException {
        Path seed = Paths.get("./data/guests-seed.csv");
        Path test = Paths.get("./data/guests-test.csv");

        Files.copy(seed, test, StandardCopyOption.REPLACE_EXISTING);
        repository = new GuestFileRepository(test.toString());
    }

    @Test
    void shouldFindAllGuests() throws DataException {
        List<Guest> guests = repository.findAll();
        assertNotNull(guests);
        assertTrue(guests.size() > 0);
    }

    @Test
    void shouldFindGuestById() {
        Guest guest = repository.findById(1);
        assertNotNull(guest);
        assertEquals(1, guest.getId());
        assertEquals("Sullivan", guest.getFirstName());
    }
}
