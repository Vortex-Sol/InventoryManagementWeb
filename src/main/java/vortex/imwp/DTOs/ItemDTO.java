package vortex.imwp.DTOs;
import vortex.imwp.Models.Warehouse;


import java.util.HashSet;
import java.util.Set;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int quantity;

    private Set<Warehouse> warehouses = new HashSet<>();

    public ItemDTO() {}
    public ItemDTO(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Set<Warehouse> getWarehouses() { return warehouses; }
    public void setWarehouses(Set<Warehouse> warehouses) {this.warehouses = warehouses; }
    public void addWarehouse(Warehouse warehouse) {this.warehouses.add(warehouse); }
}
