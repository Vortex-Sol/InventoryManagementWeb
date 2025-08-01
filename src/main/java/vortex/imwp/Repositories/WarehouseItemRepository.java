package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.WarehouseItem;
import vortex.imwp.Models.WarehouseItemID;

import java.util.List;

@Repository
public interface WarehouseItemRepository extends JpaRepository<WarehouseItem, WarehouseItemID> {
	List<WarehouseItem> findByItem(Item item);
	void deleteByItem(Item item);
}
