package vortex.imwp.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseItem> warehouseItems = new ArrayList<>();

    public Warehouse() {}
    public Warehouse(String address) { this.address = address; }

    public Long getId() { return id; }
    public String getAddress() { return address; }
    public List<WarehouseItem> getWarehouseItems() { return warehouseItems; }

    public void setId(Long id) { this.id = id; }
    public void setWarehouseItems(List<WarehouseItem> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
}
