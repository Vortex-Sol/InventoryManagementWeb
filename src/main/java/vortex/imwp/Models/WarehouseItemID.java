package vortex.imwp.Models;

import java.io.Serializable;
import java.util.Objects;

public class WarehouseItemID implements Serializable {
    private Long warehouseId;
    private String itemId;

    public WarehouseItemID(){}
    public WarehouseItemID(Long warehouseId, String itemId) {
        this.warehouseId = warehouseId;
        this.itemId = itemId;
    }

    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

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
