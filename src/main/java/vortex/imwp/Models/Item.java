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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Long barcode;
    private String sku;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseItem> warehouseItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<SaleItem> saleItems = new ArrayList<>();

    public Item() {}
    public Item(String name, String description, double price, Long barcode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }

    public Item(Long id, String name, String description, double price, Long barcode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public List<WarehouseItem> getWarehouses() { return warehouseItems; }
    public String getSku() { return sku; }
    public List<SaleItem> getSaleItems() { return saleItems; }
    public Long getBarcode() { return barcode; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setSku(String sku) { this.sku = sku; }
    public void setWarehouses(List<WarehouseItem> warehouses) {this.warehouseItems = warehouses; }
    public void addWarehouse(WarehouseItem warehouse) {this.warehouseItems.add(warehouse); }
    public void addSaleItem(SaleItem saleItem) {this.saleItems.add(saleItem);}
    public void removeSaleItem(SaleItem saleItem) {this.saleItems.remove(saleItem);}
    public void setBarcode(Long barcode) { this.barcode = barcode; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                ", sku='" + sku + '\'' +
                '}';
    }
}
