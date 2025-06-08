package htd.mastery.data;

import htd.mastery.models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepositoryDouble implements HostRepository {

    private final Host HOST;

    public HostRepositoryDouble() {
        HOST = new Host();
        HOST.setId("86f374af-ce43-450a-8326-4b9423c9fad7");
        HOST.setLastName("Sabie");
        HOST.setEmail("rsabie9w@trellian.com");
        HOST.setPhone("(317) 9110197");
        HOST.setAddress("41602 Pierstorff Plaza");
        HOST.setCity("Indianapolis");
        HOST.setState("IN");
        HOST.setPostalCode("46207");
        HOST.setStandardRate(new BigDecimal("78.00"));
        HOST.setWeekendRate(new BigDecimal("97.50"));
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
