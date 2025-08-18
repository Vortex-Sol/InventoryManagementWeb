package vortex.imwp.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Settings_Change_Log",
        indexes = {
                @Index(name="idx_scl_setting",  columnList="Setting_ID"),
                @Index(name="idx_scl_warehouse",columnList="Warehouse_ID"),
                @Index(name="idx_scl_admin",    columnList="Admin_ID"),
                @Index(name="idx_scl_changed_at",columnList="Changed_At")
        })
public class SettingsChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Setting_ID", nullable = false)
    private Long settingId;

    @Column(name = "Warehouse_ID", nullable = false)
    private Long warehouseId;

    @Column(name = "Admin_ID", nullable = false)
    private Long adminId;

    @Column(name = "Changed_At", nullable = false, updatable = false, insertable = false)
    private LocalDateTime changedAt;

    @Column(name = "Settings_Changed", nullable = false, length = 2000)
    private String settingsChanged;

    protected SettingsChangeLog() {}

    public SettingsChangeLog(Long settingId, Long warehouseId, Long adminId, LocalDateTime changedAt, String settingsChanged) {
        this.settingId = settingId;
        this.warehouseId = warehouseId;
        this.adminId = adminId;
        this.changedAt = changedAt;
        this.settingsChanged = settingsChanged;
    }

    public SettingsChangeLog(Long settingId, Long warehouseId, Long adminId , String settingsChanged) {
        this.settingId = settingId;
        this.warehouseId = warehouseId;
        this.adminId = adminId;
        this.settingsChanged = settingsChanged;
    }

    public Long getId() { return id; }
    public String getSettingsChanged() { return settingsChanged; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public Long getSettingId() { return settingId; }
    public Long getAdminId() { return adminId; }
    public Long getWarehouseId() { return warehouseId; }

    public void setId(Long id) {this.id = id;}
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    public void setSettingId(Long settingId) { this.settingId = settingId; }
    public void setChangedAt(LocalDateTime changedAt) {this.changedAt = changedAt;}
    public void setSettingsChanged(String settingsChanged) { this.settingsChanged = settingsChanged; }

}
