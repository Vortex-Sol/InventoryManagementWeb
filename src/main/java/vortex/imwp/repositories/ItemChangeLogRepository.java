package vortex.imwp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.ItemChangeLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemChangeLogRepository extends JpaRepository<ItemChangeLog, Long> {

    List<ItemChangeLog> findAllByOrderByChangedAtDesc();
    Page<ItemChangeLog> findAllByOrderByChangedAtDesc(Pageable pageable);

    List<ItemChangeLog> findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
            Long stockerId, LocalDateTime from, LocalDateTime to);
    Page<ItemChangeLog> findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
            Long stockerId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    List<ItemChangeLog> findByStockerId(Long stockerId);
    Page<ItemChangeLog> findByStockerId(Long stockerId, Pageable pageable);

    void deleteByItemId(Long itemId);
    Page<ItemChangeLog> findByWarehouseIdOrderByChangedAtDesc(Long warehouseId, Pageable pageable);

    Page<ItemChangeLog> findByStockerIdOrderByChangedAtDesc(Long settingId, Pageable pageable);
}
