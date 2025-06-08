package htd.mastery.data;

import htd.mastery.models.Guest;
import htd.mastery.models.Host;

import java.util.List;

public interface GuestRepository {
    List<Guest> findAll() throws DataException;
    Guest findById(int id) throws DataException;
    Guest findByEmail(String email) throws DataException;
}
