package vortex.imwp.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "Warehouse_Items")
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

    @Column(name = "Quantity_In_Stock")
    private Integer quantityInStock;

    @Column(nullable = false, unique = true)
    private String sku;

    @PrePersist
    public void generateSKU(){
        if (sku == null || sku.isBlank()) {
            this.sku = generateCustomSKU();
        }
    }

    /**
     * Method should stay private as it is utilized by generateSKU() under the condition sku is blank.
     * */
    private String generateCustomSKU(){
        long warehouseIdTemp = warehouse.getId();
        long itemCategoryIDTemp = item.getCategory().getId();
        String skuTemp = "";

        int counter = 0;
        while(warehouseIdTemp > 0){
            warehouseIdTemp /= 10;
            ++counter;
        }

        if(counter == 3) sku += warehouseIdTemp;
        else if (counter == 2) sku += "0" + warehouseIdTemp;
        else if (counter == 1) sku += "00" + warehouseIdTemp;
        else throw new RuntimeException("Unknown SKU id: " + warehouseIdTemp);
        skuTemp += "-";

        counter = 0;
        while(itemCategoryIDTemp > 0){
            itemCategoryIDTemp /= 10;
            ++counter;
        }

        if(counter == 3) sku += itemCategoryIDTemp;
        else if (counter == 2) sku += "0" + itemCategoryIDTemp;
        else if (counter == 1) sku += "00" + itemCategoryIDTemp;
        else throw new RuntimeException("Unknown Category id: " + itemCategoryIDTemp);
        skuTemp += "-";

        String randomSequence = "";
        counter = 6;
        while(counter > 0){
            int randomInt = (int) (Math.random() * 2);
            if(randomInt == 0) randomSequence += (char) (65+ (Math.random() * 26));
            else randomSequence += (char) (49 + (Math.random() * 11));
            --counter;
        }

        return skuTemp;
    }

    public WarehouseItem() {}
    public WarehouseItem(Warehouse warehouse, Item item, int quantityInStock) {
        this.warehouse = warehouse;
        this.item = item;
        this.quantityInStock = quantityInStock;
    }
    public WarehouseItemID getId() { return id; }
    public Warehouse getWarehouse() { return warehouse; }
    public Item getItem() { return item; }
    public int getQuantityInStock() { return quantityInStock; }

    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public void setItem(Item item) { this.item = item; }
    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }
    public void setId(WarehouseItemID id) { this.id = id; }
}
