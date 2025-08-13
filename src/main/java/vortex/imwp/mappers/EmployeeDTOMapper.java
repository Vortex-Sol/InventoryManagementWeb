package vortex.imwp.mappers;

import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Job;
import java.util.stream.Collectors;

public class EmployeeDTOMapper {
    public static EmployeeDTO map(Employee employee){
        return new EmployeeDTO(
                employee.getId(),
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
