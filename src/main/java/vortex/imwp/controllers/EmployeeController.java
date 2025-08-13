package vortex.imwp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.models.Response;
import vortex.imwp.services.EmployeeService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/users")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //TODO: GET All Users
    @GetMapping
    public ResponseEntity<Response> getEmployees() {

        Response resp = new vortex.imwp.models.Response();

        Optional<List<EmployeeDTO>> employees = employeeService.getAllEmployees();
        resp.setSuccess(employees.isPresent());
        resp.setData(employeeService.getAllEmployees());
        if (resp.isSuccess()) resp.setMessage("Employees found");
        else resp.setMessage("Employees not found");
        return ResponseEntity.ok(resp);
    }

    //TODO: GET User {id}
    @GetMapping("/{id}")
    public ResponseEntity<Response> getEmployeeById(@PathVariable Long id) {

        Response resp = new vortex.imwp.models.Response();

        Optional<EmployeeDTO> employee = employeeService.getEmployeeById(id);
        resp.setSuccess(employee.isPresent());
        resp.setData(employeeService.getEmployeeById(id));
        if (resp.isSuccess()) resp.setMessage("Employee found");
        else resp.setMessage("Employee not found");
        return ResponseEntity.ok(resp);
    }

    //TODO: PUT User {id}
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDTO employeeDto
    ) {

        Response resp = new vortex.imwp.models.Response();

        Optional<EmployeeDTO> employeeOptional = employeeService.getEmployeeById(id);
        if (employeeOptional.isEmpty()) {
            resp.setSuccess(false);
            resp.setMessage("Employee not found");
            return ResponseEntity.ok(resp);
        }

        Optional<EmployeeDTO> employeeDTO = employeeService.updateEmployee(id, employeeDto);
        if (employeeDTO.isPresent()) {
            resp.setSuccess(true);
            resp.setMessage("Employee updated");
            return ResponseEntity.ok(resp);
        }
        resp.setSuccess(false);
        resp.setMessage("Employee not found");
        resp.setData(employeeService.getEmployeeById(id));
        return ResponseEntity.ok(resp);
    }



    //TODO: DELETE User {id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteEmployee(@PathVariable Long id) {
        Response resp = new vortex.imwp.models.Response();

        Optional<EmployeeDTO> employeeOptional = employeeService.getEmployeeById(id);
        if (employeeOptional.isEmpty()) {
            resp.setSuccess(false);
            resp.setMessage("Employee not found");
            return ResponseEntity.ok(resp);
        }

        employeeService.deleteEmployee(id);
        resp.setSuccess(true);
        resp.setMessage("Employee deleted");
        return ResponseEntity.ok(resp);
    }
}
