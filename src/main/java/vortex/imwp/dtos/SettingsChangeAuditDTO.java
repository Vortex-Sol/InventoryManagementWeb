package vortex.imwp.dtos;

import java.time.LocalDateTime;

public class SettingsChangeAuditDTO {
    private Long id;
    private Long settingId;
    private Long warehouseId;
    private Long adminId;
    private LocalDateTime changedAt;
    private String settingsChanged;

    public SettingsChangeAuditDTO() {}

    public SettingsChangeAuditDTO(Long id, Long settingId, Long warehouseId, Long adminId, LocalDateTime changedAt, String settingsChanged) {
        this.id = id;
        this.settingId = settingId;
        this.warehouseId = warehouseId;
        this.adminId = adminId;
        this.changedAt = changedAt;
        this.settingsChanged = settingsChanged;
    }

    public Long getId() {return id;}
    public String getSettingsChanged() {return settingsChanged;}
    public LocalDateTime getChangedAt() {return changedAt;}
    public Long getSettingId() {return settingId;}
    public Long getWarehouseId() {return warehouseId;}
    public Long getAdminId() {return adminId;}

    public void setId(Long id) {this.id = id;}
    public void setSettingId(Long settingId) {this.settingId = settingId;}
    public void setWarehouseId(Long warehouseId) {this.warehouseId = warehouseId;}
    public void setAdminId(Long adminId) {this.adminId = adminId;}
    public void setChangedAt(LocalDateTime changedAt) {this.changedAt = changedAt;}
    public void setSettingsChanged(String settingsChanged) {this.settingsChanged = settingsChanged;}
}
