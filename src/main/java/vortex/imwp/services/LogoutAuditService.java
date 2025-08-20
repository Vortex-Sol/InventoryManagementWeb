package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LoginAudit;
import vortex.imwp.models.LogoutAudit;
import vortex.imwp.repositories.EmployeeRepository;
import vortex.imwp.repositories.LoginAuditRepository;
import vortex.imwp.repositories.LogoutAuditRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LogoutAuditService {

    private final LogoutAuditRepository logoutAuditRepository;
    private final EmployeeRepository employeeRepository;


    public LogoutAuditService(LogoutAuditRepository logoutAuditRepository, EmployeeRepository employeeRepository) {
        this.logoutAuditRepository = logoutAuditRepository;
        this.employeeRepository = employeeRepository;
    }

    public void recordLogout(String username, String ipAddress, Timestamp timestamp, String reason) {

        Optional<Employee> employee_check = employeeRepository.findByUsername(username);
        if (employee_check.isEmpty()) {
            System.out.println("Employee with username: " + username + " not found");
            return;
        }
        Employee employee = employee_check.get();
        LogoutAudit logoutAudit = new LogoutAudit(employee.getUsername(), ipAddress, timestamp, reason, employee);
        logoutAudit = logoutAuditRepository.save(logoutAudit);
        System.out.println("[" + timestamp + "] Success logout: username: " + username + " ip: " + logoutAudit.getIpAddress());
    }

    public List<LogoutAudit> getLogoutAuditsByEmployee(Employee employee) {
        return logoutAuditRepository.findByEmployee(employee);
    }

    public List<LogoutAudit> getLogoutAuditsByEmployeeAndDate(Employee employee, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
        return logoutAuditRepository.findByEmployeeAndLogoutTimeGreaterThanEqualAndLogoutTimeLessThan(
                employee, Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    public List<LogoutAudit> getLogoutAuditsByEmployeeAndPeriod(Employee employee, Timestamp start, Timestamp end) {
        return logoutAuditRepository.findByEmployeeAndLogoutTimeGreaterThanEqualAndLogoutTimeLessThan(
                employee, start, end);
    }


}
