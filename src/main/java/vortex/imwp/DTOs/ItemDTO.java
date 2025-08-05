package vortex.imwp.DTOs;
import vortex.imwp.Models.Warehouse;


import java.util.HashSet;
import java.util.Set;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity; // need to be deleted
    private Long barcode;
    private String sku;

    private Set<Warehouse> warehouses = new HashSet<>();

    public ItemDTO() {}
    public ItemDTO(String name, String description, double price, int quantity, Long barcode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.barcode = barcode;
    }
    public ItemDTO(Long id, String name, String description, double price, int quantity, Long barcode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.barcode = barcode;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Set<Warehouse> getWarehouses() { return warehouses; }
    public String getSku() { return sku; }
    public Long getBarcode() { return barcode; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setSku(String sku) { this.sku = sku; }
    public void setWarehouses(Set<Warehouse> warehouses) {this.warehouses = warehouses; }
    public void addWarehouse(Warehouse warehouse) {this.warehouses.add(warehouse); }
    public void removeWarehouse(Warehouse warehouse) {this.warehouses.remove(warehouse); }
    public void setBarcode(Long barcode) { this.barcode = barcode; }
}
