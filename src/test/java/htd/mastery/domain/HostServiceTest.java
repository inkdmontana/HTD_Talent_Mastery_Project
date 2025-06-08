package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.HostRepositoryDouble;
import htd.mastery.models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void shouldFindHostById() throws DataException {
        Host host = service.findById("86f374af-ce43-450a-8326-4b9423c9fad7");
        assertNotNull(host);
        assertEquals("Sabie", host.getLastName());
    }

    @Test
    void shouldReturnNullForUnknownHostId() throws DataException {
        Host host = service.findById("unknown-id");
        assertNull(host);
    }

    @Test
    void shouldFindAllHosts() throws DataException {
        List<Host> hosts = service.findAll();
        assertNotNull(hosts);
        assertTrue(hosts.size() > 0);
    }
}
