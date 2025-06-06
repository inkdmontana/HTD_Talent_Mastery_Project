package htd.mastery.data;

import htd.mastery.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final Host HOST;

    public HostRepositoryDouble() {
        HOST = new Host();
        HOST.setId("host-abc-123");
        HOST.setLastName("Doe");
        HOST.setEmail("host@example.com");
        HOST.setPhone("(123) 4567890");
        HOST.setAddress("123 Main St.");
        HOST.setCity("New York City");
        HOST.setState("NY");
        HOST.setPostalCode("12345");
        HOST.setStandardRate(new BigDecimal("150.00"));
        HOST.setWeekendRate(new BigDecimal("250.00"));
    }

    @Override
    public Host findById(String id) {
        return HOST.getId().equals(id) ? HOST : null;
    }

    @Override
    public List<Host> findAll() throws DataException {
        List<Host> result = new ArrayList<>();
        result.add(HOST);
        return result;
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        if (HOST.getEmail().equalsIgnoreCase(email)) {
            return HOST;
        }
        return null;
    }
}
