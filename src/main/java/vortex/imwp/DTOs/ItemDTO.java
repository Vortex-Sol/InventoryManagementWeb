package vortex.imwp.DTOs;
import vortex.imwp.Models.Warehouse;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;


    private List<Warehouse> warehouses = new ArrayList<>();

    public ItemDTO() {}
    public ItemDTO(String name, String description, BigDecimal price, List<Warehouse> warehouses) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.warehouses = warehouses;
    }

    public ItemDTO(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ItemDTO(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public List<Warehouse> getWarehouses() { return warehouses; }
    public void setWarehouses(List<Warehouse> warehouses) {this.warehouses = warehouses; }
    public void addWarehouse(Warehouse warehouse) {this.warehouses.add(warehouse); }
}
