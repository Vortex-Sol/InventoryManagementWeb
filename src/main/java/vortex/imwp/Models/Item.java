package vortex.imwp.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private Integer quantity;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<WarehouseItem> warehouseItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<SaleItem> saleItems = new ArrayList<>();

    public Item() {}
    public Item(String name, String description, double price, int quantity) {
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
    public List<WarehouseItem> getWarehouses() { return warehouseItems; }
    public void setWarehouses(List<WarehouseItem> warehouses) {this.warehouseItems = warehouses; }
    public void addWarehouse(WarehouseItem warehouse) {this.warehouseItems.add(warehouse); }
    public void addSaleItem(SaleItem saleItem) {this.saleItems.add(saleItem);}
    public void removeSaleItem(SaleItem saleItem) {this.saleItems.remove(saleItem);}
    public List<SaleItem> getSaleItems() { return saleItems; }
}
