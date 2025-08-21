package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Receipt;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findReceiptsBySale_SalesmanAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Employee salesman, LocalDateTime createdAt, LocalDateTime createdAt2);
    List<Receipt> findReceiptsBySale_Salesman_WarehouseIDAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Long warehouseID, LocalDateTime createdAt, LocalDateTime createdAt2);

}
