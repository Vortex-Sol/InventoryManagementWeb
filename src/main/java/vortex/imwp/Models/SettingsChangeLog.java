package vortex.imwp.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Settings_Change_Log")
public class SettingsChangeLog {
    @EmbeddedId
    private SettingsChangeLogID id;

    @Column(name = "Changed_At", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime changedAt;

    @Column(name = "Settings_Changed", nullable = false, length = 2000)
    private String settingsChanged;

    public SettingsChangeLog() {}
    public SettingsChangeLog(SettingsChangeLogID id, LocalDateTime changedAt, String settingsChanged) {
        this.id = id;
        this.settingsChanged = settingsChanged;
    }

    public SettingsChangeLogID getId() { return id; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getSettingsChanged() { return settingsChanged; }

    public void setId(SettingsChangeLogID id) { this.id = id; }
    public void settingsChanged(String settingsChanged) { this.settingsChanged = settingsChanged; }
}
