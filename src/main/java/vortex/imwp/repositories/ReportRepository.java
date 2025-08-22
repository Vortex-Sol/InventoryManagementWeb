package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
	boolean existsByCreatedAtWarehouseID(Long id);
	long deleteByCreatedAtWarehouseID(Long warehouseId);

}
