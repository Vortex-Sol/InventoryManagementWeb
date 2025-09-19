package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.Invoice;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	List<Invoice> findByWarehouse_Id(Long warehouseId);

}
