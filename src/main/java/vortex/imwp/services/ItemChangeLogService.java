package vortex.imwp.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemChangeLogDTO;
import vortex.imwp.mappers.ItemChangeLogDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.ItemChangeLog;
import vortex.imwp.repositories.ItemChangeLogRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemChangeLogService {

    private final ItemChangeLogRepository itemChangeLogRepository;

    public ItemChangeLogService(ItemChangeLogRepository itemChangeLogRepository) {
        this.itemChangeLogRepository = itemChangeLogRepository;
    }

    public List<ItemChangeLogDTO> getAllLogs() {
        return itemChangeLogRepository.findAllByOrderByChangedAtDesc()
                .stream().map(ItemChangeLogDTOMapper::map).toList();
    }

    public Page<ItemChangeLogDTO> getLogsPage(int page, int size) {
        return itemChangeLogRepository.findAllByOrderByChangedAtDesc(PageRequest.of(page, size))
                .map(ItemChangeLogDTOMapper::map);
    }


    public List<ItemChangeLog> getItemsChangeLogsByStocker(Employee stocker) {
        return itemChangeLogRepository.findByStockerId(stocker.getId());
    }

    public List<ItemChangeLog> getItemsChangeLogsByStockerAndDate(Employee stocker, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
        return itemChangeLogRepository.findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
                stocker.getId(), start, end);
    }

    public List<ItemChangeLog> getItemsChangeLogsByStockerAndPeriod(Employee stocker, LocalDateTime start, LocalDateTime end) {
        return itemChangeLogRepository.findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
                stocker.getId(), start, end);
    }
}
