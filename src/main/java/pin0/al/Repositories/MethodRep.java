package pin0.al.Repositories;

import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.Method;

import java.util.List;

public interface MethodRep extends CrudRepository<Method, Long> {
    List<Method> findByIdIn(List<Long> methodIds);
}
