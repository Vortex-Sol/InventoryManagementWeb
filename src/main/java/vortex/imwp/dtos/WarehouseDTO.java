package vortex.imwp.dtos;

import java.util.ArrayList;
import java.util.List;

public class WarehouseDTO {
    private Long id;
    private String phone;
    private String email;
    private String address;
    private SettingsDTO settings;
    private List<WarehouseItemDTO> warehouseItems = new ArrayList<>();
    //TODO: Data Transfer of Item Quantity in Warehouse

    public WarehouseDTO() {}
    public WarehouseDTO(Long id, String phone, String email, String address) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public WarehouseDTO(Long id, String phone, String email, String address, SettingsDTO settings) {
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.settings = settings;
    }

    public Long getId() { return id; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public List<WarehouseItemDTO> getWarehouseItems() { return warehouseItems; }
    public SettingsDTO getSettings() { return settings; }

    public void setId(Long id) { this.id = id; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setWarehouseItems(List<WarehouseItemDTO> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
    public void setSettings(SettingsDTO settings) { this.settings = settings; }
}
