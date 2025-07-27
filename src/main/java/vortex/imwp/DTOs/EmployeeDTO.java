package vortex.imwp.DTOs;
import vortex.imwp.Models.Job;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EmployeeDTO {
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private Date dob;
    private String phone;
    private String email;
    private Date startDate;
    private Date endDate;
    private Long warehouseID;
    private Long bossID;

    private Set<String> jobs = new HashSet<>();

    public EmployeeDTO() {}
    public EmployeeDTO(String username, String password, String confirmPassword, String email){
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }
    public EmployeeDTO(String username, String password, String name, String surname, String phone, String email, Date startDate, Date endDate, Long warehouseID, Set<String> jobs) {
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
    public EmployeeDTO(String username, String password, String name, String surname, String phone, String email, Date startDate, Date endDate, Long warehouseID, Long bossID, Set<String> jobs) {
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
    public String getConfirmPassword() { return confirmPassword; }
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
    public Set<String> getJobs() { return jobs; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setDob(Date dob) { this.dob = dob; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setWarehouseID(Long warehouseID) { this.warehouseID = warehouseID; }
    public void setBossID(Long bossID) { this.bossID = bossID; }
    public void addJob(String job) { jobs.add(job); }
    public void removeJob(String  job) { jobs.remove(job); }
}
