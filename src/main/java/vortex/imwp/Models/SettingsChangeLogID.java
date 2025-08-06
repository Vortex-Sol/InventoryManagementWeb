package vortex.imwp.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class SettingsChangeLogID implements Serializable{
    @Column(name = "Setting_ID")
    private Long settingID;

    @Column(name = "Warehouse_ID")
    private Long warehouseID;

    @Column(name = "Admin_ID")
    private Long adminID;

    public SettingsChangeLogID() {}
    public SettingsChangeLogID(Long settingID, Long warehouseID, Long adminID) {
        this.settingID = settingID;
        this.warehouseID = warehouseID;
        this.adminID = adminID;
    }

    public Long getSettingID() { return settingID; }
    public Long getWarehouseID() { return warehouseID; }
    public Long getAdminID() { return adminID; }

    public void setSettingID(Long settingID) { this.settingID = settingID; }
    public void setWarehouseID(Long warehouseID) { this.warehouseID = warehouseID; }
    public void setAdminID(Long adminID) { this.adminID = adminID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingsChangeLogID)) return false;
        SettingsChangeLogID that = (SettingsChangeLogID) o;
        return Objects.equals(settingID, that.settingID) &&
                Objects.equals(warehouseID, that.warehouseID) &&
                Objects.equals(adminID, that.adminID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settingID, warehouseID, adminID);
    }
}
