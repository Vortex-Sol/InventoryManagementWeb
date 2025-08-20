package vortex.imwp.models;

import jakarta.persistence.*;
import vortex.imwp.services.LogoutAuditService;

import java.util.*;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false)
    private Date dob;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;
    @Column(name = "Start_Date", nullable = false)
    private Date startDate;
    @Column(name = "End_Date")
    private Date endDate;
    @Column(name = "Warehouse_ID", nullable = false)
    private Long warehouseID;
    @Column(name = "Boss_ID")
    private Long bossID;

    @ManyToMany
    @JoinTable(
            name = "Employee_Job",
            joinColumns = @JoinColumn(name = "Employee_ID"),
            inverseJoinColumns = @JoinColumn(name = "Job_ID")
    )
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "salesman")
    private List<Sale> sales = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<LoginAudit> loginAudits = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<LogoutAudit> logoutAudits = new ArrayList<>();

    public Employee() {}
    public Employee(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public Employee(String username, String password, String name, String surname, String phone, String email, Date startDate, Date endDate, Long warehouseID) {}
    public Employee(String username, String password, String name, String surname, String phone, String email, Date startDate, Date endDate, Long warehouseID, List<Job> jobs) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.warehouseID = warehouseID;
        this.jobs = jobs;
    }
    public Employee(String username, String password, String name, String surname, String phone, String email, Date startDate, Date endDate, Long warehouseID, Long bossID, List<Job> jobs) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
        this.warehouseID = warehouseID;
        this.bossID = bossID;
        this.jobs = jobs;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public Date getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public Long getWarehouseID() { return warehouseID; }
    public Long getBossID() { return bossID; }
    public List<Job> getJobs() { return jobs; }
    public List<Sale> getSales() { return sales; }
    public List<LoginAudit> getLoginAudits() { return loginAudits; }
    public List<LogoutAudit> getLogoutAudits() { return logoutAudits; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setDob(Date dob) { this.dob = dob; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setWarehouseID(Long warehouseID) { this.warehouseID = warehouseID; }
    public void setBossID(Long bossID) { this.bossID = bossID; }
    public void setJobs(List<Job> jobs) { this.jobs = jobs; }
    public void setSales(List<Sale> sales) { this.sales = sales; }
    public void setLoginAudits(List<LoginAudit> loginAudits) { this.loginAudits = loginAudits; }
    public void setLogoutAudits(List<LogoutAudit> logoutAudits) { this.logoutAudits = logoutAudits; }

    public void addLoginAudit(LoginAudit loginAudit) { this.loginAudits.add(loginAudit); }
    public void removeLoginAudit(LoginAudit loginAudit) { this.loginAudits.remove(loginAudit); }

    public void addLogoutAudit(LogoutAudit logoutAudit) { this.logoutAudits.add(logoutAudit); }
    public void removeLogoutAudit(LogoutAudit logoutAudit) { this.logoutAudits.remove(logoutAudit); }

    public void addSale(Sale sale) { this.sales.add(sale); }
    public void removeSale(Sale sale) { this.sales.remove(sale); }

    public void addJob(Job job) { jobs.add(job); }
    public void removeJob(Job job) { jobs.remove(job); }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", warehouseID=" + warehouseID +
                ", bossID=" + bossID +
                ", jobs=" + jobs +
                ", sales=" + sales +
                ", loginAudits=" + loginAudits +
                ", logoutAudits=" + logoutAudits +
                '}';
    }
}
