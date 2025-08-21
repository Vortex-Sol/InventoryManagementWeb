package vortex.imwp.mappers;

import vortex.imwp.dtos.ItemChangeAuditDTO;
import vortex.imwp.models.ItemChangeAudit;

public class ItemChangeAuditDTOMapper {

    public static ItemChangeAuditDTO map(ItemChangeAudit itemChangeAudit) {
        return new ItemChangeAuditDTO(
                itemChangeAudit.getItemId(),
                itemChangeAudit.getItemId(),
                itemChangeAudit.getStockerId(),
                itemChangeAudit.getWarehouseId(),
                itemChangeAudit.getChangedAt(),
                itemChangeAudit.getDataChanged()
        );
    }

    public static ItemChangeAudit map(ItemChangeAuditDTO itemChangeAuditDTO) {
        return new ItemChangeAudit(
                itemChangeAuditDTO.getItemId(),
                itemChangeAuditDTO.getStockerId(),
                itemChangeAuditDTO.getWarehouseId(),
                itemChangeAuditDTO.getChangedAt(),
                itemChangeAuditDTO.getDataChanged()
        );
    }
}
