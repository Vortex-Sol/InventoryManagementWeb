package vortex.imwp.DTOs;
import vortex.imwp.Models.Category;
import vortex.imwp.Models.SaleItem;
import vortex.imwp.Models.Warehouse;
import vortex.imwp.Models.WarehouseItem;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long barcode;
    private CategoryDTO category;
    private List<WarehouseItemDTO> warehouses = new ArrayList<>();
    private List<SaleItemDTO> saleItems = new ArrayList<>();

    public ItemDTO() {}
    public ItemDTO(String name, String description, Double price, Long barcode, CategoryDTO category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.category = category;
    }
    public ItemDTO(Long id, String name, String description, Double price, Long barcode, CategoryDTO category, List<WarehouseItemDTO> warehouses, List<SaleItemDTO> saleItems) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.category = category;
        this.warehouses = warehouses;
        this.saleItems = saleItems;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public List<WarehouseItemDTO> getWarehouses() { return warehouses; }
    public Long getBarcode() { return barcode; }
    public CategoryDTO getCategory() { return category; }
    public List<SaleItemDTO> getSaleItems() { return saleItems; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setWarehouses(List<WarehouseItemDTO> warehouses) {this.warehouses = warehouses; }
    public void setBarcode(Long barcode) { this.barcode = barcode; }
    public void setCategory(CategoryDTO category) { this.category = category; }
    public void setSaleItems(List<SaleItemDTO> saleItems) { this.saleItems = saleItems;}

    public void addWarehouse(WarehouseItemDTO warehouse) {this.warehouses.add(warehouse); }
    public void removeWarehouse(WarehouseItemDTO warehouse) {this.warehouses.remove(warehouse); }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                '}';
    }
}
