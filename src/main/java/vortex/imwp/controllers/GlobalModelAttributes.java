package vortex.imwp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.mappers.WarehouseDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Warehouse;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.WarehouseService;

import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

	private final EmployeeService employeeService;
	private final WarehouseService warehouseService;

	public GlobalModelAttributes(EmployeeService employeeService, WarehouseService warehouseService) {
		this.employeeService = employeeService;
		this.warehouseService = warehouseService;
	}

	@ModelAttribute("warehouse")
	public WarehouseDTO populateWarehouse(Authentication authentication) {
		if (authentication == null) return null;
		Employee employee = employeeService.getEmployeeByAuthentication(authentication);
		if (employee == null || employee.getWarehouseID() == null) return null;

		Optional<Warehouse> wh = warehouseService.getWarehouseById(employee.getWarehouseID());
		return wh.map(WarehouseDTOMapper::map).orElse(null);
	}
}
