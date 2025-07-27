package vortex.imwp.DTOs;

import jakarta.persistence.ManyToMany;
import vortex.imwp.Models.Employee;

import java.util.List;

public class JobDTO {
    private String name;
    private String description;
    private List<EmployeeDTO> employees;

    public JobDTO(){}
    public JobDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public JobDTO(String name, String description, List<EmployeeDTO> employees) {
        this.name = name;
        this.description = description;

    }

    public String getName() {return this.name;}
    public String getDescription() {return this.description;}
    public List<EmployeeDTO> getEmployees() {return this.employees;}
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setEmployees(List<EmployeeDTO> employees) { this.employees = employees; }
    public void addEmployee(EmployeeDTO employee) { this.employees.add(employee); }
}
