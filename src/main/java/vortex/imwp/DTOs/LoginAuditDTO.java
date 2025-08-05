package vortex.imwp.DTOs;

import vortex.imwp.Models.Employee;

import java.sql.Timestamp;

public class LoginAuditDTO {
    private Long id;
    private String username;
    private String ipAddress;
    private Timestamp loginDate;
    private boolean successFailure;
    private Employee employee;

    public LoginAuditDTO() {}

    public LoginAuditDTO(String username, String ipAddress, Timestamp loginDate, boolean successFailure, Employee employee) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginDate = loginDate;
        this.successFailure = successFailure;
        this.employee = employee;
    }

    public LoginAuditDTO(Long id, String username, String ipAddress, Timestamp loginDate, boolean successFailure, Employee employee) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginDate = loginDate;
        this.successFailure = successFailure;
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

    public Timestamp getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Timestamp loginDate) {
        this.loginDate = loginDate;
    }

    public boolean getSuccessFailure() {
        return successFailure;
    }

    public void setSuccessFailure(boolean successFailure) {
        this.successFailure = successFailure;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
