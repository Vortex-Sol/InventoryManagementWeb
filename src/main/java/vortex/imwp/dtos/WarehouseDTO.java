package vortex.imwp.dtos;

import java.util.ArrayList;
import java.util.List;

public class WarehouseDTO {
    private Long id;
    private String address;
    private List<WarehouseItemDTO> warehouseItems = new ArrayList<>();
    //TODO: Data Transfer of Item Quantity in Warehouse

    public WarehouseDTO() {}
    public WarehouseDTO(Long id, String address) { this.address = address; }

    public Long getId() { return id; }
    public String getAddress() { return address; }
    public List<WarehouseItemDTO> getWarehouseItems() { return warehouseItems; }

    public void setId(Long id) { this.id = id; }
    public void setWarehouseItems(List<WarehouseItemDTO> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
}
