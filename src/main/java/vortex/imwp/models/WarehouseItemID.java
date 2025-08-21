package vortex.imwp.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WarehouseItemID implements Serializable {
    private Long warehouseId;
    private Long itemId;

    public WarehouseItemID(){}
    public WarehouseItemID(Long warehouseId, Long itemId) {
        this.warehouseId = warehouseId;
        this.itemId = itemId;
    }

    public Long getWarehouseId() { return warehouseId; }
    public Long getItemId() { return itemId; }

    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WarehouseItemID)) return false;
        WarehouseItemID that = (WarehouseItemID) o;
        return Objects.equals(warehouseId, that.warehouseId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(warehouseId, itemId);
    }
}
