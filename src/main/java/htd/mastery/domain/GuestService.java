package htd.mastery.domain;

import htd.mastery.data.DataException;
import htd.mastery.data.GuestRepository;
import htd.mastery.models.Guest;

import java.util.List;

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
}
