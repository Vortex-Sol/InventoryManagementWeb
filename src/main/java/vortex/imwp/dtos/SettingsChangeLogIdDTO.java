package vortex.imwp.dtos;

import java.io.Serializable;
import java.util.Objects;

public class SettingsChangeLogIdDTO implements Serializable {
    private Long settingID;
    private Long warehouseID;
    private Long adminID;

    public SettingsChangeLogIdDTO() {}
    public SettingsChangeLogIdDTO(Long settingID, Long warehouseID, Long adminID) {
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
    public int hashCode() {
        return Objects.hash(settingID, warehouseID, adminID);
    }
}
