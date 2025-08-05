package vortex.imwp.Models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "Login_Audit")
public class LoginAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(name = "IP_Address", nullable = false)
    private String ipAddress;
    @Column(name = "Login_Time", nullable = false)
    private Timestamp loginTime;
    @Column(name = "Success_Failure", nullable = false)
    private boolean successFailure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    public LoginAudit() {}

    public LoginAudit(String username, String ipAddress, Timestamp loginTime, boolean successFailure, Employee employee) {
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
        this.successFailure = successFailure;
        this.employee = employee;
    }

    public LoginAudit(Long id, String username, String ipAddress, Timestamp loginTime, boolean successFailure, Employee employee) {
        this.id = id;
        this.username = username;
        this.ipAddress = ipAddress;
        this.loginTime = loginTime;
        this.successFailure = successFailure;
        this.employee = employee;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getIpAddress() { return ipAddress; }
    public Timestamp getLoginTime() { return loginTime; }
    public boolean getSuccessFailure() { return successFailure; }
    public Employee getEmployee() { return employee; }

    public void setUsername(String username) { this.username = username; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public void setLoginTime(Timestamp loginTime) { this.loginTime = loginTime; }
    public void setSuccessFailure(boolean successFailure) { this.successFailure = successFailure; }
    public void setEmployee(Employee employee) { this.employee = employee; }


}
