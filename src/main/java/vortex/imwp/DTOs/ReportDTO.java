package vortex.imwp.DTOs;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportDTO {
    private String type;
    private Long EmployeeIdCreated;
    private Long createdAtWarehouseID;
    private Long ReportID;
    private List<ItemDTO> items;

    public ReportDTO() {}
    public ReportDTO(String type, Long employeeIdCreated, Long createdAtWarehouseID, Long ReportID) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
        this.ReportID = ReportID;
        items = new ArrayList<ItemDTO>();
    }

    public String getType() { return type; }
    public Long getEmployeeIdCreated() { return EmployeeIdCreated; }
    public Long getCreatedAtWarehouseID() { return createdAtWarehouseID; }
    public Long getReportID() { return ReportID; }
    public List<ItemDTO> getItems() { return items; }

    public void setType(String type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }
    public void setReportID(Long ReportID) { this.ReportID = ReportID; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    public void addItem(ItemDTO item) { this.items.add(item); }
    public void removeItem(ItemDTO item) { this.items.remove(item); }

    public JSONObject generateReport() { return new JSONObject(); }
}
