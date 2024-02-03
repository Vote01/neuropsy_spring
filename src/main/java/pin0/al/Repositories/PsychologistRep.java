package pin0.al.Repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pin0.al.Models.Method;
import pin0.al.Models.Psychologist;
import pin0.al.Models.Specialization;

import java.awt.print.Pageable;
import java.util.List;

public interface PsychologistRep extends CrudRepository<Psychologist, Long> {

    List<Psychologist> findByMethodListInAndSpecializationListIn(List<Method> methodList, List<Specialization> specializationList);
    List<Psychologist> findByMethodListIn(List<Method> methodList);
    List<Psychologist> findBySpecializationListIn(List<Specialization> specializationList);
    List<Psychologist> findByConfirmedTrue();
    long countByVideoconTrue();
    long count();
    @Query(value = "SELECT m.METHOD_NAME, COUNT(p.psychologist_id) FROM Method m JOIN PSYCHOLOGIST_METHOD p ON m.method_id = p.method_id GROUP BY m.METHOD_NAME ORDER BY COUNT(p.psychologist_id) DESC", nativeQuery = true)
    List<Object[]> findPopularMethods();



}
