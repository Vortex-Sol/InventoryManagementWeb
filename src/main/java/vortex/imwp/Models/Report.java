package vortex.imwp.Models;

import jakarta.persistence.*;
import org.json.simple.JSONObject;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private Long EmployeeIdCreated;
    @Column(nullable = false)
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

    public void setType(String type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }

    public JSONObject generateReport() { return new JSONObject(); }
}
