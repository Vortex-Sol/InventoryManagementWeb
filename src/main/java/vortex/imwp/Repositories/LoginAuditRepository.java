package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.LoginAudit;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {

    List<LoginAudit> findByEmployee(Employee employee);
    List<LoginAudit> findByEmployeeAndLoginTimeBetween(Employee employee, Timestamp start, Timestamp end);
    List<LoginAudit> findByEmployeeAndLoginTimeGreaterThanEqualAndLoginTimeLessThan(
            Employee employee, Timestamp start, Timestamp end);
}
