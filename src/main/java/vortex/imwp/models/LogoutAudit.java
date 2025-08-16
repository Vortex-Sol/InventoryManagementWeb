package vortex.imwp.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Logout_Audit")
public class LogoutAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(name = "IP_Address", nullable = false)
    private String ipAddress;
    @Column(name = "Logout_Time", nullable = false)
    private Timestamp logoutTime;
    @Column(name = "Logout_Reason", nullable = false)
    private String logoutReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public LogoutAudit(Long id, String username, String ipAddress, Timestamp logoutTime, String logoutReason, Employee employee) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.logoutTime = logoutTime;
        this.logoutReason = logoutReason;
        this.employee = employee;
    }

    public LogoutAudit(String username, String ipAddress, Timestamp logoutTime, String logoutReason, Employee employee) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.logoutTime = logoutTime;
        this.logoutReason = logoutReason;
        this.employee = employee;
    }

    public LogoutAudit() {}

    public Long getId() { return id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Timestamp logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "LogoutAudit{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", logoutTime=" + logoutTime +
                ", logoutReason='" + logoutReason + '\'' +
                ", employeeID=" + employee.getId() +
                '}';
    }
}
