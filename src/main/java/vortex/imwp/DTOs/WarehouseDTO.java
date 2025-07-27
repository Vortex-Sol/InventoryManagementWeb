package vortex.imwp.DTOs;

import jakarta.persistence.*;
import vortex.imwp.Models.WarehouseItem;

import java.util.ArrayList;
import java.util.List;

public class WarehouseDTO {

    private String address;
    private List<WarehouseItem> warehouseItems = new ArrayList<>();
    //TODO: Data Transfer of Item Quantity in Warehouse

    public WarehouseDTO() {}
    public WarehouseDTO(String address) { this.address = address; }
    public String getAddress() { return address; }
    public List<WarehouseItem> getWarehouseItems() { return warehouseItems; }
    public void setWarehouseItems(List<WarehouseItem> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
}
