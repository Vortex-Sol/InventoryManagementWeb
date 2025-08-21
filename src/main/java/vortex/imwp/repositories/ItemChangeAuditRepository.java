package vortex.imwp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.ItemChangeAudit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemChangeAuditRepository extends JpaRepository<ItemChangeAudit, Long> {

    List<ItemChangeAudit> findAllByOrderByChangedAtDesc();
    Page<ItemChangeAudit> findAllByOrderByChangedAtDesc(Pageable pageable);

    List<ItemChangeAudit> findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
            Long stockerId, LocalDateTime from, LocalDateTime to);
    Page<ItemChangeAudit> findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
            Long stockerId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    List<ItemChangeAudit> findByStockerId(Long stockerId);
    Page<ItemChangeAudit> findByStockerId(Long stockerId, Pageable pageable);


    Page<ItemChangeAudit> findByWarehouseIdOrderByChangedAtDesc(Long warehouseId, Pageable pageable);

    Page<ItemChangeAudit> findByStockerIdOrderByChangedAtDesc(Long settingId, Pageable pageable);
}
