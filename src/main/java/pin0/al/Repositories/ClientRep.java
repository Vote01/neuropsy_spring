package pin0.al.Repositories;

import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.Client;

public interface ClientRep extends CrudRepository<Client, Long> {
}
