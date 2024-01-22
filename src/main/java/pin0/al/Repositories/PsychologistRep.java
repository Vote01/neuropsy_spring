package pin0.al.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pin0.al.Models.Method;
import pin0.al.Models.Psychologist;
import pin0.al.Models.Specialization;

import java.util.List;

public interface PsychologistRep extends CrudRepository<Psychologist, Long> {

    List<Psychologist> findByMethodListInAndSpecializationListIn(List<Method> methodList, List<Specialization> specializationList);
    List<Psychologist> findByMethodListIn(List<Method> methodList);
    List<Psychologist> findBySpecializationListIn(List<Specialization> specializationList);


}
