package vortex.imwp.Models;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private Date dob;
    private String phone;
    private String email;
    private Date startDate;
    private Date endDate;
    private Long warehouseID;
    private Long bossID;

    @ManyToMany
    @JoinTable(
            name = "Employee_Job",
            joinColumns = @JoinColumn(name = "Employee_ID"),
            inverseJoinColumns = @JoinColumn(name = "Job_ID")
    )
    private List<Job> jobs = new ArrayList<>();

    @OneToMany(mappedBy = "salesman")
    private List<Sale> sales;

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
    public void addJob(Job job) { jobs.add(job); }
    public void removeJob(Job job) { jobs.remove(job); }
}
