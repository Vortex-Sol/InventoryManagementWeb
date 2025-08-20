package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Sale;
import vortex.imwp.models.SaleItem;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

	void deleteSaleItemByItemId(long itemId);
}