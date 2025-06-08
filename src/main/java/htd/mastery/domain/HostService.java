package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.HostRepository;
import htd.mastery.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostService {

    private final HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public List<Host> findAll() throws DataException {
        return repository.findAll();
    }

    public Host findById(String id) throws DataException {
        return repository.findById(id);
    }

    public Host findByEmail(String email) throws DataException {
        return repository.findByEmail(email);
    }
}
