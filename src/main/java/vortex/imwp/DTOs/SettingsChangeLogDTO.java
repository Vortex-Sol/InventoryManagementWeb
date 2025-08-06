package vortex.imwp.DTOs;

import vortex.imwp.Models.SettingsChangeLogID;

import java.time.LocalDateTime;

public class SettingsChangeLogDTO {
    private SettingsChangeLogIdDTO id;
    private LocalDateTime changedAt;
    private String settingsChanged;

    public SettingsChangeLogDTO() {}
    public SettingsChangeLogDTO(SettingsChangeLogIdDTO id, LocalDateTime changedAt, String settingsChanged) {
        this.id = id;
        this.changedAt = changedAt;
        this.settingsChanged = settingsChanged;
    }

    public SettingsChangeLogIdDTO getId() { return id; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getSettingsChanged() { return settingsChanged; }

    public void setId(SettingsChangeLogIdDTO id) { this.id = id; }
    public void settingsChanged(String settingsChanged) { this.settingsChanged = settingsChanged; }
}
