package vortex.imwp.Models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String address;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseItem> warehouseItems = new ArrayList<>();

    public Warehouse() {}
    public Warehouse(String address) { this.address = address; }
    public long getId() { return id; }
    public String getAddress() { return address; }
    public List<WarehouseItem> getWarehouseItems() { return warehouseItems; }
    public void setWarehouseItems(List<WarehouseItem> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
}
