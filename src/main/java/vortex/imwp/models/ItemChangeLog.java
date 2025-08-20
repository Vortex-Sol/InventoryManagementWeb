package vortex.imwp.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Item_Change_Log",
        indexes = {
                @Index(name=)
        })
public class ItemChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Item_ID", nullable = false)
    private Long  itemId;

    @Column(name = "Stocker_ID", nullable = false)
    private Long stockerId;

    @Column(name = "Warehouse_ID", nullable = false)
    private Long warehouseId;

    @Column(name = "Changed_At", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name = "Data_Changed", nullable = false, length = 2000)
    private String dataChanged;

    public ItemChangeLog() {}
    public ItemChangeLog(Long itemId, Long stockerId, Long warehouseId, LocalDateTime changedAt, String dataChanged) {
        this.itemId = itemId;
        this.stockerId = stockerId;
        this.warehouseId = warehouseId;
        this.changedAt = changedAt;
        this.dataChanged = dataChanged;
    }

    public Long getItemId() { return this.itemId; }
    public Long getStockerId() { return this.stockerId; }
    public Long getWarehouseId() { return this.warehouseId; }
    public LocalDateTime getChangedAt() { return this.changedAt; }
    public String getDataChanged() { return this.dataChanged; }

    public void setItemId(Long itemId) { this.itemId = itemId; }
    public void setStockerId(Long stockerId) { this.stockerId = stockerId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
    public void setDataChanged(String dataChanged) { this.dataChanged = dataChanged; }
}
