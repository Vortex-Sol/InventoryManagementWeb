package vortex.imwp.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.EmployeeDTO;
import vortex.imwp.Mappers.EmployeeDTOMapper;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Job;
import vortex.imwp.Repositories.EmployeeRepository;
import vortex.imwp.Repositories.JobRepository;

import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<EmployeeDTO> getEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username)
                .map(EmployeeDTOMapper::map);
    }

    public Employee registerEmployee(EmployeeDTO employee) {
        Employee user = new Employee();
        user.setUsername(employee.getUsername());
        user.setEmail(employee.getEmail());
        user.setPassword(passwordEncoder.encode(employee.getPassword()));
        employeeRepository.save(user);
        System.out.println("Registered employee " + user.getUsername());
        return user;
    }
}
