package vortex.imwp.DTOs;

import java.io.Serializable;
import java.util.Objects;

public class WarehouseItemIdDTO implements Serializable{
    private Long warehouseId;
    private Long itemId;

    public WarehouseItemIdDTO(){}
    public WarehouseItemIdDTO(Long warehouseId, Long itemId) {
        this.warehouseId = warehouseId;
        this.itemId = itemId;
    }

    public Long getWarehouseId() { return warehouseId; }
    public Long getItemId() { return itemId; }

    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, itemId);
    }
}
