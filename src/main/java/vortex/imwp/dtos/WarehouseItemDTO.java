package vortex.imwp.dtos;

import vortex.imwp.models.WarehouseItemID;

public class WarehouseItemDTO {
    private WarehouseItemID id = new WarehouseItemID();
    private WarehouseDTO warehouse;
    private ItemDTO item;
    private int quantityInStock;

    public WarehouseItemDTO() {}
    public WarehouseItemDTO(WarehouseDTO warehouse, ItemDTO item, int quantityInStock) {
        this.warehouse = warehouse;
        this.item = item;
        this.quantityInStock = quantityInStock;
    }

    public WarehouseItemID getId() { return id; }
    public ItemDTO getItem() { return item; }
    public WarehouseDTO getWarehouse() { return warehouse; }
    public int getQuantityInStock() { return quantityInStock; }

    public void setId(WarehouseItemID id) { this.id = id; }
    public void setItem(ItemDTO item) { this.item = item; }
    public void setWarehouse(WarehouseDTO warehouse) { this.warehouse = warehouse; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    @Override
    public String toString() {
        return "WarehouseID: " + warehouse.getId()
                + ", ItemID: " + item.getId()
                + ", QuantityInStock: " + quantityInStock;
    }
}
