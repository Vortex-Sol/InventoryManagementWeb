package vortex.imwp.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.mappers.WarehouseDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
	private final WarehouseRepository warehouseRepository;
	private final SettingsService settingsService;
	private final ItemService itemService;
	private final EmployeeRepository employeeRepository;
	private final ReportRepository reportRepository;
	private final ItemChangeLogRepository itemChangeLogRepository;
	private final SettingsChangeAuditRepository settingsChangeAuditRepository;

	public WarehouseService(WarehouseRepository warehouseRepository, SettingsService settingsService,
							ItemService itemService, EmployeeRepository employeeRepository,
							ReportRepository reportRepository, ItemChangeLogRepository itemChangeLogRepository,
							SettingsChangeAuditRepository settingsChangeAuditRepository) {
		this.warehouseRepository = warehouseRepository;
		this.settingsService = settingsService;
		this.itemService = itemService;
		this.employeeRepository = employeeRepository;
		this.reportRepository = reportRepository;
		this.itemChangeLogRepository = itemChangeLogRepository;
		this.settingsChangeAuditRepository = settingsChangeAuditRepository;
	}

	public List<WarehouseDTO> getAllWarehouses() {
		Iterable<Warehouse> list = warehouseRepository.findAll();
		List<WarehouseDTO> warehouses = new ArrayList<>();

		for (Warehouse warehouse : list) {
			WarehouseDTO warehouseDTO = WarehouseDTOMapper.map(warehouse);
			warehouses.add(warehouseDTO);
		}
		return warehouses;
	}
	@Transactional
	public Warehouse createWarehouseBasic(String phone, String email, String address){
		return new Warehouse(phone, email, address);
	}

	public Optional<WarehouseDTO> getWarehouseDTOById(Long id) {
		return getWarehouseById(id).map(WarehouseDTOMapper::map);
	}

	public Optional<Warehouse> getWarehouseById(Long id) {
		return warehouseRepository.findById(id);
	}

	public Warehouse addWarehouse(Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}

    @Transactional
    public void updateWarehouse(Long id, WarehouseDTO dto) {
        warehouseRepository.findById(id).map(
                existing -> {
                    existing.setAddress(dto.getAddress());
                    existing.setEmail(dto.getEmail());
                    existing.setPhone(dto.getPhone());

                    Warehouse saved = warehouseRepository.save(existing);
                    return WarehouseDTOMapper.map(saved);
                }
        );
    }
	@Transactional
	public void upsertSettingsForWarehouse(Long id, TaxRate taxRate) {
		Warehouse warehouse = getWarehouseById(id)
				.orElseThrow(() -> new EntityNotFoundException("Warehouse " + id + " not found"));

		if (warehouse.getSettings() == null) {
			settingsService.createDefaultSettingsForWarehouse(warehouse, taxRate);
		} else {
			settingsService.updateTaxRate(warehouse.getSettings().getId(), taxRate);
		}
	}

    public Long generateId(){
        Warehouse warehouse = warehouseRepository.findTopByOrderByIdDesc();
        return warehouse.getId() + 1;
    }

	@Transactional
	public void deleteWarehouse(Long id) {
		Warehouse warehouse = getWarehouseById(id)
				.orElseThrow(() -> new EntityNotFoundException("Warehouse " + id + " not found"));

		if (employeeRepository.existsByWarehouseID(id)) {
			throw new IllegalStateException("Cannot delete warehouse with assigned employees.");
		}
		if (reportRepository.existsByCreatedAtWarehouseID(id)) {
			throw new IllegalStateException("Cannot delete warehouse with reports.");
		}

		settingsChangeAuditRepository.deleteByWarehouseId(id);
		itemChangeLogRepository.deleteByWarehouseId(id);

		warehouseRepository.delete(warehouse);
	}
}
