package vortex.imwp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Settings_Change_Log")
public class SettingsChangeAudit {
    @EmbeddedId
    private SettingsChangeAuditID id;

    @Column(name = "Changed_At", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime changedAt;

    @Column(name = "Settings_Changed", nullable = false, length = 2000)
    private String settingsChanged;

    public SettingsChangeAudit() {}
    public SettingsChangeAudit(SettingsChangeAuditID id, LocalDateTime changedAt, String settingsChanged) {
        this.id = id;
        this.changedAt = changedAt;
        this.settingsChanged = settingsChanged;
    }

    public SettingsChangeAudit(String settingsChanged, SettingsChangeAuditID id) {
        this.settingsChanged = settingsChanged;
        this.changedAt = LocalDateTime.now();
        this.id = id;
    }

    public SettingsChangeAuditID getId() { return id; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getSettingsChanged() { return settingsChanged; }

    public void setId(SettingsChangeAuditID id) { this.id = id; }
    public void settingsChanged(String settingsChanged) { this.settingsChanged = settingsChanged; }
}
