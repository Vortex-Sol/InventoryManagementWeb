package vortex.imwp.Models;

import jakarta.persistence.*;
import org.json.simple.JSONObject;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Type", nullable = false)
    private String type;
    @Column(name = "Employee_ID_Created", nullable = false)
    private Long EmployeeIdCreated;
    @Column(name = "Created_At_Warehouse_ID", nullable = false)
    private Long createdAtWarehouseID;

    public Report() {}
    public Report(String type, Long employeeIdCreated, Long createdAtWarehouseID) {
        this.type = type;
        EmployeeIdCreated = employeeIdCreated;
        this.createdAtWarehouseID = createdAtWarehouseID;
    }

    public Long getId() { return id; }
    public String getType() { return type; }
    public Long getEmployeeIdCreated() { return EmployeeIdCreated; }
    public Long getCreatedAtWarehouseID() { return createdAtWarehouseID; }

    public void setId(Long id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }

    public JSONObject generateTodayGeneralReport() { return new JSONObject(); }
    public JSONObject generateTodaySaleReport() { return new JSONObject(); }
    public JSONObject generateTodayInventoryReport() { return new JSONObject(); }
    public JSONObject generateTodayEmployeeReport() { return new JSONObject(); }

    public JSONObject generatePeriodGeneralReport() { return new JSONObject(); }
    public JSONObject generatePeriodSaleReport() { return new JSONObject(); }
    public JSONObject generatePeriodInventoryReport() { return new JSONObject(); }
    public JSONObject generatePeriodEmployeeReport() { return new JSONObject(); }
}
