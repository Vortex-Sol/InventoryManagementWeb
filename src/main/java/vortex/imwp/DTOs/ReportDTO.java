package vortex.imwp.DTOs;

import org.json.simple.JSONObject;

public class ReportDTO {
    private String type;
    private Long EmployeeIdCreated;
    private Long createdAtWarehouseID;

    public ReportDTO() {}
    public ReportDTO(String type, Long employeeIdCreated, Long createdAtWarehouseID) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
    }

    public String getType() { return type; }
    public Long getEmployeeIdCreated() { return EmployeeIdCreated; }
    public Long getCreatedAtWarehouseID() { return createdAtWarehouseID; }

    public void setType(String type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }

    public JSONObject generateReport() { return new JSONObject(); }
}
