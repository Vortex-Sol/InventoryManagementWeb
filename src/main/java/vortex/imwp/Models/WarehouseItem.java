package vortex.imwp.Models;

import jakarta.persistence.*;

@Entity
public class WarehouseItem {
    @EmbeddedId
    private WarehouseItemID id = new WarehouseItemID();

    @ManyToOne
    @MapsId("warehouseId")
    @JoinColumn(name = "Warehouse_ID")
    private Warehouse warehouse;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "Item_ID")
    private Item item;

    private int quantityInStock;

    public WarehouseItem() {}
    public WarehouseItem(Warehouse warehouse, Item item, int quantityInStock) {
        this.warehouse = warehouse;
        this.item = item;
        this.quantityInStock = quantityInStock;
    }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public int getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }
    public WarehouseItemID getId() { return id; }
    public void setId(WarehouseItemID id) { this.id = id; }
}
