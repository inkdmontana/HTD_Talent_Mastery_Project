package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.GuestRepository;
import htd.mastery.models.Guest;
import htd.mastery.models.Host;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public List<Guest> findAll() throws DataException {
        return repository.findAll();
    }

    public Guest findById(int id) throws DataException {
        return repository.findById(id);
    }

    public Guest findByEmail(String email) throws DataException {
        return repository.findByEmail(email);
    }
}
