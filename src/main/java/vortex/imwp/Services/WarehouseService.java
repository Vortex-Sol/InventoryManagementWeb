package vortex.imwp.Services;

import vortex.imwp.Models.Warehouse;
import vortex.imwp.Repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
	private final WarehouseRepository warehouseRepository;
	public WarehouseService(WarehouseRepository warehouseRepository) {
		this.warehouseRepository = warehouseRepository;
	}

	public List<Warehouse> getAllWarehouses() {
		return warehouseRepository.findAll();
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
