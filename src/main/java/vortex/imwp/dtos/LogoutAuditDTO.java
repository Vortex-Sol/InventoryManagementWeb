package vortex.imwp.dtos;

import java.sql.Timestamp;

public class LogoutAuditDTO {
    private Long id;
    private String username;
    private String ipAddress;
    private Timestamp logoutTime;
    private String logoutReason;
    private EmployeeDTO employee;

    public LogoutAuditDTO() { }

    public LogoutAuditDTO(String username, String ipAddress, Timestamp logoutTime, String logoutReason, EmployeeDTO employee) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.logoutTime = logoutTime;
        this.logoutReason = logoutReason;
        this.employee = employee;
    }

    public LogoutAuditDTO(Long id, String username, String ipAddress, Timestamp logoutTime, String logoutReason, EmployeeDTO employee) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.logoutTime = logoutTime;
        this.logoutReason = logoutReason;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }
}
