package vortex.imwp.Models;

import jakarta.persistence.*;
import org.json.simple.JSONObject;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @Column(name = "Employee_ID_Created")
    private Long EmployeeIdCreated;

    @Column(name = "Created_At_Warehouse_ID")
    private Long createdAtWarehouseID;

    @Lob
    @Column(name = "Data", nullable = false)
    private String data;

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
    public String getData() { return data; }

    public void setType(String type) { this.type = type; }
    public void setEmployeeIdCreated(Long employeeIdCreated) { EmployeeIdCreated = employeeIdCreated; }
    public void setCreatedAtWarehouseID(Long createdAtWarehouseID) { this.createdAtWarehouseID = createdAtWarehouseID; }
    public void setData(String data) { this.data = data; }

    public JSONObject generateReport() { return new JSONObject(); }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", EmployeeIdCreated=" + EmployeeIdCreated +
                ", createdAtWarehouseID=" + createdAtWarehouseID +
                '}';
    }
}
