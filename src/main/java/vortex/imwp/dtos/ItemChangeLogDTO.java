package vortex.imwp.dtos;
import java.time.LocalDateTime;

public class ItemChangeLogDTO {
    private Long id;
    private Long  itemId;
    private Long stockerId;
    private Long warehouseId;
    private LocalDateTime changedAt;
    private String dataChanged;

    public ItemChangeLogDTO() {}
    public ItemChangeLogDTO(Long id, Long itemId, Long stockerId, Long warehouseId, LocalDateTime changedAt, String dataChanged) {
        this.id = id;
        this.itemId = itemId;
        this.stockerId = stockerId;
        this.warehouseId = warehouseId;
        this.changedAt = changedAt;
        this.dataChanged = dataChanged;
    }
    public Long getId() { return id; }
    public Long getItemId() { return this.itemId; }
    public Long getStockerId() { return this.stockerId; }
    public Long getWarehouseId() { return this.warehouseId; }
    public LocalDateTime getChangedAt() { return this.changedAt; }
    public String getDataChanged() { return this.dataChanged; }

    public void setId(Long id) { this.id = id; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public void setStockerId(Long stockerId) { this.stockerId = stockerId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public void setDataChanged(String dataChanged) { this.dataChanged = dataChanged; }
}
