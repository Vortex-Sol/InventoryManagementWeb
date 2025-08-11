package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Sale;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query(
            value = """
      SELECT
        ID,
        Sale_Time,
        Salesman_ID
      FROM Sale
      WHERE Sale_Time BETWEEN :start AND :end
      ORDER BY Sale_Time ASC
      """,
            nativeQuery = true
    )
    List<Sale> findSalesBetweenTimestamps(
                    @Param("start") Timestamp start,
                    @Param("end")   Timestamp end
            );

    List<Sale> findSalesBySalesman(Employee salesman);

    List<Sale> findSalesBySalesmanAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(Employee salesman, Timestamp start, Timestamp end);
    List<Sale> findSalesBySalesman_WarehouseIDAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(Long warehouseID, Timestamp start, Timestamp end);
}
