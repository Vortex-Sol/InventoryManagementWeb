package vortex.imwp.Models;

import jakarta.persistence.*;
import org.json.simple.JSONObject;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false, length = 50)
    private ReportType.Type type;
    @Column(name = "Employee_ID_Created", nullable = false)
    private Long EmployeeIdCreated;
    @Column(name = "Created_At_Warehouse_ID", nullable = false)
    private Long createdAtWarehouseID;
    @Column(name = "Data", columnDefinition = "CLOB", nullable = false)
    private String data;

    public Report() {}
    public Report(ReportType.Type type, Long employeeIdCreated, Long createdAtWarehouseID) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
    }
    public Report(ReportType.Type type, Long employeeIdCreated, Long createdAtWarehouseID, JSONObject data) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
        this.data = data.toJSONString();
    }

    public Long getId() { return id; }
    public ReportType.Type getType() { return type; }
    public Long getEmployeeIdCreated() { return EmployeeIdCreated; }
    public Long getCreatedAtWarehouseID() { return createdAtWarehouseID; }
    public String getData() { return data; }

    public void setId(Long id) { this.id = id; }
    public void setType(ReportType.Type type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }
    public void setData(JSONObject data) { this.data = data.toJSONString(); }

    public JSONObject generateTodayGeneralReport() { return new JSONObject(); }
    public JSONObject generateTodaySaleReport() { return new JSONObject(); }
    public JSONObject generateTodayInventoryReport() { return new JSONObject(); }
    public JSONObject generateTodayEmployeeReport() { return new JSONObject(); }

    public JSONObject generatePeriodGeneralReport() { return new JSONObject(); }
    public JSONObject generatePeriodSaleReport() { return new JSONObject(); }
    public JSONObject generatePeriodInventoryReport() { return new JSONObject(); }
    public JSONObject generatePeriodEmployeeReport() { return new JSONObject(); }
}
