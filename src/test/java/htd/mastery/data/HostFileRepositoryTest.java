package htd.mastery.data;

import htd.mastery.models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepositoryTest {

    static final String SEED_PATH = "./data/hosts-seed.csv";
    static final String TEST_PATH = "./data/hosts-test.csv";

    HostFileRepository repository;

    @BeforeEach
    void setUp() throws Exception {

        Files.copy(Path.of(SEED_PATH), Path.of(TEST_PATH), StandardCopyOption.REPLACE_EXISTING);
        repository = new HostFileRepository(TEST_PATH);
    }

    @Test
    void shouldFindAllHosts() throws DataException {
        List<Host> hosts = repository.findAll();
        assertNotNull(hosts);
        assertTrue(hosts.size() >= 1);
    }

    @Test
    void shouldFindHostById() throws DataException {
        Host host = repository.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(host);
        assertEquals("Yearnes", host.getLastName());
    }

    @Test
    void shouldFindHostByEmail_caseInsensitive() throws DataException {
        Host host = repository.findByEmail("EYEARNES0@sfgate.com");
        assertNotNull(host);
        assertEquals("3edda6bc-ab95-49a8-8962-d50b53f84b15", host.getId());
    }

    @Test
    void shouldReturnNullForMissingId() {
        Host host = repository.findById("fake-id");
        assertNull(host);
    }

    @Test
    void shouldReturnNullForMissingEmail() throws DataException {
        Host host = repository.findByEmail("fake@email.com");
        assertNull(host);
    }

    @Test
    void shouldReturnNullForNullEmail() throws DataException {
        Host host = repository.findByEmail(null);
        assertNull(host);
    }

    @Test
    void shouldReturnNullForBlankEmail() throws DataException {
        Host host = repository.findByEmail("   ");
        assertNull(host);
    }
}
