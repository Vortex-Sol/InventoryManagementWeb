package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LogoutAudit;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LogoutAuditRepository extends JpaRepository<LogoutAudit, Long> {

    List<LogoutAudit> findByEmployee(Employee employee);
    List<LogoutAudit> findByEmployeeAndLogoutTimeGreaterThanEqualAndLogoutTimeLessThan(
            Employee employee, Timestamp start, Timestamp end);
}
