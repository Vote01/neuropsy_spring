package pin0.al.Repositories;

import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.User;

public interface UserRep extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
