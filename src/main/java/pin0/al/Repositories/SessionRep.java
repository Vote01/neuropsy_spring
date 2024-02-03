package pin0.al.Repositories;

import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.Client;
import pin0.al.Models.Psychologist;
import pin0.al.Models.Session;

import java.util.List;

public interface SessionRep extends CrudRepository<Session, Long> {

    List<Session> findByPsychologist(Psychologist psychologist);
    List<Session> findByClient(Client client);
    long countByStatus(String status);

}
