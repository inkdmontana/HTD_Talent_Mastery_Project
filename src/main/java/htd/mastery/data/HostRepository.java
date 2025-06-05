package htd.mastery.data;

import htd.mastery.models.Host;

import java.util.List;

public interface HostRepository {
    List<Host> findAll() throws DataException;

    Host findById(String id) throws DataException;

    Host findByEmail(String email) throws DataException;
}
