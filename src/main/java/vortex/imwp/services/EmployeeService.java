package vortex.imwp.services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.mappers.EmployeeDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LoginAudit;
import vortex.imwp.models.LogoutAudit;
import vortex.imwp.repositories.EmployeeRepository;
import vortex.imwp.repositories.JobRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JobRepository jobRepository;
    private final LoginAuditService loginAuditService;
    private final LogoutAuditService logoutAuditService;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JobRepository jobRepository, LoginAuditService loginAuditService, LogoutAuditService logoutAuditService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jobRepository = jobRepository;
        this.loginAuditService = loginAuditService;
        this.logoutAuditService = logoutAuditService;
    }

    public Optional<EmployeeDTO> getEmployeeByUsername(String username){
        return employeeRepository.findByUsernameWithJobs(username)
                .map(EmployeeDTOMapper::map);
    }

    public Employee getEmployeeByAuthentication(Authentication authentication){
        return employeeRepository.getByUsername(authentication.getName());
    }

    public List<Employee> getAllEmployeesFromWarehouse(Long warehouseID){
        List<Employee> employeesAll = employeeRepository.findAll();
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : employeesAll) {
            if (employee.getWarehouseID().equals(warehouseID)) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> getAllEmployeesFromWarehouseWithJob(Long warehouseID, String jobName){
        List<Employee> employeesAll = employeeRepository.findAll();
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : employeesAll) {
            if (employee.getWarehouseID().equals(warehouseID) &&
            employee.getJobs().stream().anyMatch(job -> jobName.equals(job.getName()))) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> getAllActiveTodayEmployeesFromWarehouse(Long warehouseID, LocalDate date){
        List<Employee> employeesAll = getAllEmployeesFromWarehouse(warehouseID);
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : employeesAll) {
            List<LoginAudit> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndDate(employee, date);
            List<LogoutAudit> logoutAudits = logoutAuditService.getLogoutAuditsByEmployeeAndDate(employee, date);
            if(!loginAudits.isEmpty() || !logoutAudits.isEmpty()) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public List<Employee> getAllActivePeriodEmployeesFromWarehouse(Long warehouseID, Timestamp start, Timestamp end){
        List<Employee> employeesAll = getAllEmployeesFromWarehouse(warehouseID);
        List<Employee> employees = new ArrayList<>();
        for (Employee employee : employeesAll) {
            List<LoginAudit> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndPeriod(employee, start, end);
            List<LogoutAudit> logoutAudits = logoutAuditService.getLogoutAuditsByEmployeeAndPeriod(employee, start, end);
            if(!loginAudits.isEmpty() || !logoutAudits.isEmpty()) {
                employees.add(employee);
            }
        }
        return employees;
    }

    public Employee registerEmployee(EmployeeDTO employee) {
        Employee user = new Employee();
        user.setUsername(employee.getUsername());
        user.setEmail(employee.getEmail());
        user.setPassword(passwordEncoder.encode(employee.getPassword()));
        user.setName("Name");
        user.setSurname("Surname");
        user.setDob(new Date());
        user.setStartDate(new Date());
        user.setPhone("TBD");
        user.setBossID(0L);
        user.setWarehouseID(0L);
        user.addJob(jobRepository.findById(0L));
        employeeRepository.save(user);
        return user;
    }

    public Optional<List<EmployeeDTO>> getAllEmployees() {
        Iterable<Employee> list = employeeRepository.findAll();
        List<EmployeeDTO> employees = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Employee employee : list) employees.add(EmployeeDTOMapper.map(employee));
            return Optional.of(employees);
        }
        return Optional.empty();
    }

    public Optional<EmployeeDTO> getEmployeeById(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(EmployeeDTOMapper::map);
    }

    @Transactional
    public Optional<EmployeeDTO> updateEmployee(Long id, EmployeeDTO employee) {
        return employeeRepository.findById(id).map(
                existing -> {
                    existing.setUsername(employee.getUsername());
                    existing.setPassword(employee.getPassword());
                    existing.setName(employee.getName());
                    existing.setSurname(employee.getSurname());
                    existing.setDob(employee.getDob());
                    existing.setPhone(employee.getPhone());
                    existing.setEmail(employee.getEmail());
                    existing.setStartDate(employee.getStartDate());
                    existing.setEndDate(employee.getEndDate());
                    existing.setWarehouseID(employee.getWarehouseID());
                    existing.setBossID(employee.getBossID());

                    Employee saved = employeeRepository.save(existing);
                    return EmployeeDTOMapper.map(saved);
                }
        );

    }

    @Transactional
    public void deleteEmployee(long id) {
        employeeRepository.deleteById(id);
    }

    @Transactional
    public void assignRole(Long userId, String roleName) {
        var employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + userId));
        var job = jobRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        boolean hasRole = employee.getJobs().stream()
                .anyMatch(j -> roleName.equals(j.getName()));
        if (!hasRole) {
            employee.getJobs().add(job);
            employeeRepository.save(employee);
        }
    }

    @Transactional
    public void removeRole(Long userId, String roleName) {
        var employee = employeeRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + userId));

        employee.getJobs().removeIf(j -> roleName.equals(j.getName()));
        employeeRepository.save(employee);
    }
}
