package vortex.imwp.Mappers;

import vortex.imwp.DTOs.EmployeeDTO;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Job;

import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeDTOMapper {
    public static EmployeeDTO map(Employee employee){
        return new EmployeeDTO(
                employee.getUsername(),
                employee.getPassword(),
                employee.getName(),
                employee.getSurname(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getStartDate(),
                employee.getEndDate(),
                employee.getWarehouseID(),
                employee.getJobs()
                        .stream()
                        .map(Job::getName)
                        .collect(Collectors.toSet())
        );
    }

    public static Employee map(EmployeeDTO employeeDTO) {
        return new Employee(
                employeeDTO.getUsername(),
                employeeDTO.getPassword(),
                employeeDTO.getEmail()
        );
    }
}
