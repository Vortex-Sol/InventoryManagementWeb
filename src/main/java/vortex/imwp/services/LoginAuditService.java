package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LoginAudit;
import vortex.imwp.repositories.EmployeeRepository;
import vortex.imwp.repositories.LoginAuditRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LoginAuditService {

    private final LoginAuditRepository loginAuditRepository;
    private final EmployeeRepository employeeRepository;

    public LoginAuditService(LoginAuditRepository loginAuditRepository, EmployeeRepository employeeRepository) {
        this.loginAuditRepository = loginAuditRepository;
        this.employeeRepository = employeeRepository;
    }

    public Long recordLogin(String username, String ipAddress, Timestamp timestamp, boolean success) {

        Optional<Employee> employee_check = employeeRepository.findByUsername(username);
        if (employee_check.isEmpty()) {
            System.out.println("Employee with username: " + username + " not found");
            return null;
        }
        Employee employee = employee_check.get();
        LoginAudit loginAudit = new LoginAudit(employee.getUsername(), ipAddress, timestamp, success, employee);
        loginAudit = loginAuditRepository.save(loginAudit);
        return loginAudit.getId();
    }

    public List<LoginAudit> getLoginAuditsByEmployee(Employee employee) {
        return loginAuditRepository.findByEmployee(employee);
    }

    public List<LoginAudit> getLoginAuditsByEmployeeAndDate(Employee employee, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
        return loginAuditRepository.findByEmployeeAndLoginTimeGreaterThanEqualAndLoginTimeLessThan(
                employee, Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    public List<LoginAudit> getLoginAuditsByEmployeeAndPeriod(Employee employee, Timestamp start, Timestamp end) {
        return loginAuditRepository.findByEmployeeAndLoginTimeGreaterThanEqualAndLoginTimeLessThan(
                employee, start, end);
    }

}
