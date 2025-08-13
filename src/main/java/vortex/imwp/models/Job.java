package vortex.imwp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "jobs")
    private List<Employee> employees;

    public Job(){}
    public Job(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() {return this.name;}
    public String getDescription() {return this.description;}
    public List<Employee> getEmployees() {return this.employees;}

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
