package pin0.al.Repositories;

import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.Specialization;

import java.util.List;

public interface SpecializationRep extends CrudRepository<Specialization, Long> {
    List<Specialization> findByIdIn(List<Long> specializationIds);
}
