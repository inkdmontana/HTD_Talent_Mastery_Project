package htd.mastery.data;

import htd.mastery.models.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HostFileRepository implements HostRepository {
    private final String filePath;

    public HostFileRepository(@Value("${hostFilePath}") String filePath) { this.filePath = filePath;}

    @Override
    public Host findById(String id) {
        try {
            return findAll().stream()
                    .filter(h -> h.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        } catch (DataException e) {
            return null;
        }
    }

    @Override
    public List<Host> findAll() throws DataException {
        List<Host> hosts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // skip header
            String line;
            while ((line = reader.readLine()) != null) {
                Host host = deserialize(line);
                if (host != null) {
                    hosts.add(host);
                }
            }
        } catch (IOException ex) {
            throw new DataException("Could not read host data", ex);
        }

        return hosts;
    }

    @Override
    public Host findByEmail(String email) throws DataException {
        if (email == null || email.isBlank()) {
            return null;
        }
        return findAll().stream()
                .filter(h -> h.getEmail() != null && h.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    private Host deserialize(String line) {
        String[] fields = line.split(",", -1);
        if (fields.length < 10) return null;

        Host host = new Host();
        host.setId(fields[0]);
        host.setLastName(fields[1].replace("@@@", ","));
        host.setEmail(fields[2]);
        host.setPhone(fields[3]);
        host.setAddress(fields[4].replace("@@@", ","));
        host.setCity(fields[5]);
        host.setState(fields[6]);
        host.setPostalCode(fields[7]);
        host.setStandardRate(new BigDecimal(fields[8]));
        host.setWeekendRate(new BigDecimal(fields[9]));

        return host;
    }

    private String serialize(Host host) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                host.getId(),
                host.getLastName().replace(",", "@@@"),
                host.getEmail(),
                host.getPhone(),
                host.getAddress().replace(",", "@@@"),
                host.getCity(),
                host.getState(),
                host.getPostalCode(),
                host.getStandardRate(),
                host.getWeekendRate());
    }

}
