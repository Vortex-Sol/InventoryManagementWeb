package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LoginAudit;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {

    List<LoginAudit> findByEmployee(Employee employee);
    List<LoginAudit> findByEmployeeAndLoginTimeBetween(Employee employee, Timestamp start, Timestamp end);
    List<LoginAudit> findByEmployeeAndLoginTimeGreaterThanEqualAndLoginTimeLessThan(
            Employee employee, Timestamp start, Timestamp end);
}
