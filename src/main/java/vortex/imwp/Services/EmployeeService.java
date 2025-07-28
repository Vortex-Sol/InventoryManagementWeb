package vortex.imwp.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.EmployeeDTO;
import vortex.imwp.Mappers.EmployeeDTOMapper;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Job;
import vortex.imwp.Repositories.EmployeeRepository;
import vortex.imwp.Repositories.JobRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JobRepository jobRepository;
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, JobRepository jobRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jobRepository = jobRepository;
    }

    public Optional<EmployeeDTO> getEmployeeByUsername(String username){
        return employeeRepository.findByUsernameWithJobs(username)
                .map(EmployeeDTOMapper::map);
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
}
