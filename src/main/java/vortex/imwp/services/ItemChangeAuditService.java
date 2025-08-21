package vortex.imwp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemChangeAuditDTO;
import vortex.imwp.mappers.ItemChangeAuditDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.ItemChangeAudit;
import vortex.imwp.repositories.ItemChangeAuditRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemChangeAuditService {

    private final ItemChangeAuditRepository itemChangeAuditRepository;

    public ItemChangeAuditService(ItemChangeAuditRepository itemChangeAuditRepository) {
        this.itemChangeAuditRepository = itemChangeAuditRepository;
    }

    public List<ItemChangeAuditDTO> getAllAudits() {
        return itemChangeAuditRepository.findAllByOrderByChangedAtDesc()
                .stream().map(ItemChangeAuditDTOMapper::map).toList();
    }

    public Page<ItemChangeAuditDTO> getAuditsPage(int page, int size) {
        return itemChangeAuditRepository.findAllByOrderByChangedAtDesc(PageRequest.of(page, size))
                .map(ItemChangeAuditDTOMapper::map);
    }

    public List<ItemChangeAudit> getItemsChangeAuditsByStocker(Employee stocker) {
        return itemChangeAuditRepository.findByStockerId(stocker.getId());
    }

    public List<ItemChangeAudit> getItemsChangeAuditsByStockerAndDate(Employee stocker, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
        return itemChangeAuditRepository.findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
                stocker.getId(), start, end);
    }

    public List<ItemChangeAudit> getItemsChangeAuditsByStockerAndPeriod(Employee stocker, LocalDateTime start, LocalDateTime end) {
        return itemChangeAuditRepository.findByStockerIdAndChangedAtBetweenOrderByChangedAtDesc(
                stocker.getId(), start, end);
    }
}
