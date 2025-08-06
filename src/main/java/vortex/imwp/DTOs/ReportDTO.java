package vortex.imwp.DTOs;

import org.json.simple.JSONObject;
import vortex.imwp.Models.ReportType;

public class ReportDTO {
    private ReportType.Type type;
    private Long EmployeeIdCreated;
    private Long createdAtWarehouseID;
    private String data;

    public ReportDTO() {}
    public ReportDTO(ReportType.Type type, Long employeeIdCreated, Long createdAtWarehouseID) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
    }
    public ReportDTO(ReportType.Type type, Long employeeIdCreated, Long createdAtWarehouseID, JSONObject data) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
        this.data = data.toJSONString();
    }

    public ReportType.Type getType() { return type; }
    public Long getEmployeeIdCreated() { return EmployeeIdCreated; }
    public Long getCreatedAtWarehouseID() { return createdAtWarehouseID; }
    public String getData() { return data; }

    public void setType(ReportType.Type type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }
    public void setData(String data) { this.data = data; }

    public JSONObject generateReport() { return new JSONObject(); }
}
