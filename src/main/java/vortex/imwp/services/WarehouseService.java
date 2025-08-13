package vortex.imwp.services;

import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.mappers.WarehouseDTOMapper;
import vortex.imwp.models.Warehouse;
import vortex.imwp.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
	private final WarehouseRepository warehouseRepository;
	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
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

	public Optional<Warehouse> getWarehouseById(Long id) {
		return warehouseRepository.findById(id);
	}

	public Warehouse addWarehouse(Warehouse warehouse) {
		return warehouseRepository.save(warehouse);
	}

	public void deleteWarehouse(Long id) {
		warehouseRepository.deleteById(id);
	}
}
